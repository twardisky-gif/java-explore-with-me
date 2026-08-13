package ru.practicum.ewm.service;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.dto.Location;
import ru.practicum.ewm.dto.NewEventDto;
import ru.practicum.ewm.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.dto.UpdateEventUserRequest;
import ru.practicum.ewm.entity.Category;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.entity.User;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.EntityMapper;
import ru.practicum.ewm.model.AdminStateAction;
import ru.practicum.ewm.model.EventSort;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.UserStateAction;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.OffsetPageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class EventService {
    private static final int UNLIMITED_PARTICIPANT_LIMIT = 0;
    private static final int USER_EVENT_MIN_HOURS = 2;
    private static final int ADMIN_EVENT_MIN_HOURS = 1;
    private final EventRepository eventRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final StatsGateway statsGateway;

    public EventService(EventRepository eventRepository, UserService userService, CategoryService categoryService,
                        StatsGateway statsGateway) {
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.categoryService = categoryService;
        this.statsGateway = statsGateway;
    }

    @Transactional
    public EventFullDto create(Long userId, NewEventDto request) {
        validateFutureDate(request.eventDate(), USER_EVENT_MIN_HOURS);
        User initiator = userService.getEntity(userId);
        Category category = categoryService.getEntity(request.category());
        Event event = new Event();
        event.setAnnotation(request.annotation());
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setDescription(request.description());
        event.setEventDate(request.eventDate());
        event.setInitiator(initiator);
        event.setLatitude(request.location().lat());
        event.setLongitude(request.location().lon());
        event.setPaid(Boolean.TRUE.equals(request.paid()));
        event.setParticipantLimit(request.participantLimit() == null
                ? UNLIMITED_PARTICIPANT_LIMIT : request.participantLimit());
        event.setRequestModeration(request.requestModeration() == null || request.requestModeration());
        event.setState(EventState.PENDING);
        event.setTitle(request.title());
        return EntityMapper.toEventFullDto(eventRepository.save(event), 0);
    }

    @Transactional(readOnly = true)
    public List<EventShortDto> getUserEvents(Long userId, int from, int size) {
        userService.getEntity(userId);
        Page<Event> page = eventRepository.findByInitiatorId(userId,
                new OffsetPageRequest(from, size, Sort.by("id")));
        Map<Long, Long> views = statsGateway.loadViews(page.getContent());
        return page.stream().map(event -> EntityMapper.toEventShortDto(event, views.getOrDefault(event.getId(), 0L)))
                .toList();
    }

    @Transactional(readOnly = true)
    public EventFullDto getUserEvent(Long userId, Long eventId) {
        userService.getEntity(userId);
        Event event = getOwnedEvent(userId, eventId);
        return EntityMapper.toEventFullDto(event, statsGateway.loadViews(List.of(event)).getOrDefault(eventId, 0L));
    }

    @Transactional
    public EventFullDto updateByUser(Long userId, Long eventId, UpdateEventUserRequest request) {
        userService.getEntity(userId);
        Event event = getOwnedEvent(userId, eventId);
        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Only pending or canceled events can be changed");
        }
        if (request.eventDate() != null) {
            validateFutureDate(request.eventDate(), USER_EVENT_MIN_HOURS);
        }
        applyUpdate(event, request.annotation(), request.category(), request.description(), request.eventDate(),
                request.location(), request.paid(), request.participantLimit(), request.requestModeration(),
                request.title());
        if (request.stateAction() == UserStateAction.SEND_TO_REVIEW) {
            event.setState(EventState.PENDING);
        } else if (request.stateAction() == UserStateAction.CANCEL_REVIEW) {
            event.setState(EventState.CANCELED);
        }
        Event saved = eventRepository.save(event);
        return EntityMapper.toEventFullDto(saved, statsGateway.loadViews(List.of(saved)).getOrDefault(eventId, 0L));
    }

    @Transactional(readOnly = true)
    public List<EventFullDto> getAdminEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                              LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                              int from, int size) {
        validateRange(rangeStart, rangeEnd);
        Specification<Event> specification = buildSpecification(null, users, states, categories, null,
                rangeStart, rangeEnd, false, false);
        Page<Event> page = eventRepository.findAll(specification,
                new OffsetPageRequest(from, size, Sort.by("id")));
        Map<Long, Long> views = statsGateway.loadViews(page.getContent());
        return page.stream().map(event -> EntityMapper.toEventFullDto(event, views.getOrDefault(event.getId(), 0L)))
                .toList();
    }

    @Transactional
    public EventFullDto updateByAdmin(Long eventId, UpdateEventAdminRequest request) {
        Event event = getEvent(eventId);
        if (request.eventDate() != null) {
            validateFutureDate(request.eventDate(), ADMIN_EVENT_MIN_HOURS);
        }
        applyUpdate(event, request.annotation(), request.category(), request.description(), request.eventDate(),
                request.location(), request.paid(), request.participantLimit(), request.requestModeration(),
                request.title());
        if (request.stateAction() == AdminStateAction.PUBLISH_EVENT) {
            if (event.getState() != EventState.PENDING) {
                throw new ConflictException("Cannot publish the event because it is not pending");
            }
            validatePublishDate(event.getEventDate());
            event.setState(EventState.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        } else if (request.stateAction() == AdminStateAction.REJECT_EVENT) {
            if (event.getState() == EventState.PUBLISHED) {
                throw new ConflictException("Cannot reject a published event");
            }
            event.setState(EventState.CANCELED);
        }
        Event saved = eventRepository.save(event);
        return EntityMapper.toEventFullDto(saved, statsGateway.loadViews(List.of(saved)).getOrDefault(eventId, 0L));
    }

    @Transactional(readOnly = true)
    public List<EventShortDto> getPublicEvents(String text, List<Long> categories, Boolean paid,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                boolean onlyAvailable, EventSort sort, int from, int size,
                                                String uri, String ip) {
        LocalDateTime effectiveStart = rangeStart == null ? LocalDateTime.now() : rangeStart;
        validateRange(effectiveStart, rangeEnd);
        statsGateway.recordHit(uri, ip);
        Specification<Event> specification = buildSpecification(text, null, List.of(EventState.PUBLISHED),
                categories, paid, effectiveStart, rangeEnd, true, onlyAvailable);
        Sort databaseSort = sort == EventSort.EVENT_DATE ? Sort.by("eventDate") : Sort.by("id");
        if (sort == EventSort.VIEWS) {
            Page<Event> all = eventRepository.findAll(specification,
                    new OffsetPageRequest(0, Integer.MAX_VALUE, databaseSort));
            Map<Long, Long> views = statsGateway.loadViews(all.getContent());
            List<Event> sorted = new ArrayList<>(all.getContent());
            sorted.sort(Comparator.comparing((Event event) -> views.getOrDefault(event.getId(), 0L)).reversed()
                    .thenComparing(Event::getId));
            return slice(sorted, from, size).stream()
                    .map(event -> EntityMapper.toEventShortDto(event, views.getOrDefault(event.getId(), 0L)))
                    .toList();
        }
        Page<Event> page = eventRepository.findAll(specification,
                new OffsetPageRequest(from, size, databaseSort));
        Map<Long, Long> views = statsGateway.loadViews(page.getContent());
        return page.stream().map(event -> EntityMapper.toEventShortDto(event, views.getOrDefault(event.getId(), 0L)))
                .toList();
    }

    @Transactional(readOnly = true)
    public EventFullDto getPublicEvent(Long eventId, String uri, String ip) {
        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        statsGateway.recordHit(uri, ip);
        long views = statsGateway.loadViews(List.of(event)).getOrDefault(eventId, 0L);
        return EntityMapper.toEventFullDto(event, views);
    }

    @Transactional(readOnly = true)
    public Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
    }

    private Event getOwnedEvent(Long userId, Long eventId) {
        return eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
    }

    private void applyUpdate(Event event, String annotation, Long categoryId, String description,
                             LocalDateTime eventDate, Location location, Boolean paid, Integer participantLimit,
                             Boolean requestModeration, String title) {
        if (annotation != null) {
            event.setAnnotation(annotation);
        }
        if (categoryId != null) {
            event.setCategory(categoryService.getEntity(categoryId));
        }
        if (description != null) {
            event.setDescription(description);
        }
        if (eventDate != null) {
            event.setEventDate(eventDate);
        }
        if (location != null) {
            event.setLatitude(location.lat());
            event.setLongitude(location.lon());
        }
        if (paid != null) {
            event.setPaid(paid);
        }
        if (participantLimit != null) {
            event.setParticipantLimit(participantLimit);
        }
        if (requestModeration != null) {
            event.setRequestModeration(requestModeration);
        }
        if (title != null) {
            event.setTitle(title);
        }
    }

    private Specification<Event> buildSpecification(String text, List<Long> users, List<EventState> states,
                                                     List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                                     LocalDateTime rangeEnd, boolean publicSearch,
                                                     boolean onlyAvailable) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (text != null && !text.isBlank()) {
                String pattern = "%" + text.toLowerCase() + "%";
                predicates.add(builder.or(builder.like(builder.lower(root.get("annotation")), pattern),
                        builder.like(builder.lower(root.get("description")), pattern)));
            }
            if (users != null && !users.isEmpty()) {
                predicates.add(root.get("initiator").get("id").in(users));
            }
            if (states != null && !states.isEmpty()) {
                predicates.add(root.get("state").in(states));
            }
            if (categories != null && !categories.isEmpty()) {
                predicates.add(root.get("category").get("id").in(categories));
            }
            if (paid != null) {
                predicates.add(builder.equal(root.get("paid"), paid));
            }
            if (rangeStart != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("eventDate"), rangeStart));
            }
            if (rangeEnd != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("eventDate"), rangeEnd));
            }
            if (publicSearch && onlyAvailable) {
                predicates.add(builder.or(builder.equal(root.get("participantLimit"), UNLIMITED_PARTICIPANT_LIMIT),
                        builder.lessThan(root.get("confirmedRequests"), root.get("participantLimit"))));
            }
            return builder.and(predicates.toArray(Predicate[]::new));
        };
    }

    private void validateFutureDate(LocalDateTime eventDate, int minimumHours) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(minimumHours))) {
            throw new BadRequestException("Event date must be at least " + minimumHours + " hours in the future");
        }
    }

    private void validatePublishDate(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(ADMIN_EVENT_MIN_HOURS))) {
            throw new ConflictException("Event date must be at least one hour after publication");
        }
    }

    private void validateRange(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("Range start must not be after range end");
        }
    }

    private List<Event> slice(List<Event> events, int from, int size) {
        if (from >= events.size()) {
            return List.of();
        }
        int to = Math.min(events.size(), from + size);
        return events.subList(from, to);
    }
}
