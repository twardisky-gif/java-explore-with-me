package ru.practicum.ewm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.entity.ParticipationRequest;
import ru.practicum.ewm.entity.User;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.EntityMapper;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.RequestStatus;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RequestService {
    private static final int UNLIMITED_PARTICIPANT_LIMIT = 0;
    private static final int MIN_CONFIRMED_REQUESTS = 0;
    private static final int REQUEST_COUNT_STEP = 1;
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserService userService;

    public RequestService(RequestRepository requestRepository, EventRepository eventRepository,
                          UserService userService) {
        this.requestRepository = requestRepository;
        this.eventRepository = eventRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        userService.getEntity(userId);
        return requestRepository.findByRequesterId(userId).stream().map(EntityMapper::toRequestDto).toList();
    }

    @Transactional
    public ParticipationRequestDto add(Long userId, Long eventId) {
        User requester = userService.getEntity(userId);
        Event event = getLockedEvent(eventId);
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("The initiator cannot request participation in their own event");
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Cannot participate in an unpublished event");
        }
        if (requestRepository.existsByEventIdAndRequesterId(eventId, userId)) {
            throw new ConflictException("A participation request already exists");
        }
        boolean limited = event.getParticipantLimit() > UNLIMITED_PARTICIPANT_LIMIT;
        if (limited && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictException("The participant limit has been reached");
        }
        ParticipationRequest request = new ParticipationRequest();
        request.setCreated(LocalDateTime.now());
        request.setEvent(event);
        request.setRequester(requester);
        boolean autoConfirm = event.getParticipantLimit() == UNLIMITED_PARTICIPANT_LIMIT
                || !event.isRequestModeration();
        request.setStatus(autoConfirm ? RequestStatus.CONFIRMED : RequestStatus.PENDING);
        if (autoConfirm) {
            event.setConfirmedRequests(event.getConfirmedRequests() + REQUEST_COUNT_STEP);
            eventRepository.save(event);
        }
        return EntityMapper.toRequestDto(requestRepository.save(request));
    }

    @Transactional
    public ParticipationRequestDto cancel(Long userId, Long requestId) {
        userService.getEntity(userId);
        ParticipationRequest request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Request with id=" + requestId + " was not found"));
        if (request.getStatus() == RequestStatus.CONFIRMED) {
            Event event = getLockedEvent(request.getEvent().getId());
            event.setConfirmedRequests(Math.max(MIN_CONFIRMED_REQUESTS,
                    event.getConfirmedRequests() - REQUEST_COUNT_STEP));
            eventRepository.save(event);
        }
        request.setStatus(RequestStatus.CANCELED);
        return EntityMapper.toRequestDto(requestRepository.save(request));
    }

    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        userService.getEntity(userId);
        ensureOwner(eventId, userId);
        return requestRepository.findByEventId(eventId).stream().map(EntityMapper::toRequestDto).toList();
    }

    @Transactional
    public EventRequestStatusUpdateResult updateStatuses(Long userId, Long eventId,
                                                          EventRequestStatusUpdateRequest update) {
        userService.getEntity(userId);
        Event event = getLockedEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
        if (update.status() != RequestStatus.CONFIRMED && update.status() != RequestStatus.REJECTED) {
            throw new BadRequestException("Only CONFIRMED and REJECTED statuses are allowed");
        }
        List<ParticipationRequest> requests = requestRepository.findByIdInAndEventId(update.requestIds(), eventId);
        if (requests.size() != update.requestIds().size()) {
            throw new NotFoundException("One or more requests were not found");
        }
        if (requests.stream().anyMatch(request -> request.getStatus() != RequestStatus.PENDING)) {
            throw new ConflictException("Only pending requests can be changed");
        }
        List<ParticipationRequest> confirmed = new ArrayList<>();
        List<ParticipationRequest> rejected = new ArrayList<>();
        if (update.status() == RequestStatus.REJECTED) {
            requests.forEach(request -> request.setStatus(RequestStatus.REJECTED));
            rejected.addAll(requests);
        } else {
            if (event.getParticipantLimit() > UNLIMITED_PARTICIPANT_LIMIT
                    && event.getConfirmedRequests() >= event.getParticipantLimit()) {
                throw new ConflictException("The participant limit has been reached");
            }
            for (ParticipationRequest request : requests) {
                if (event.getParticipantLimit() == UNLIMITED_PARTICIPANT_LIMIT
                        || event.getConfirmedRequests() < event.getParticipantLimit()) {
                    request.setStatus(RequestStatus.CONFIRMED);
                    event.setConfirmedRequests(event.getConfirmedRequests() + REQUEST_COUNT_STEP);
                    confirmed.add(request);
                } else {
                    request.setStatus(RequestStatus.REJECTED);
                    rejected.add(request);
                }
            }
            if (event.getParticipantLimit() > UNLIMITED_PARTICIPANT_LIMIT
                    && event.getConfirmedRequests() >= event.getParticipantLimit()) {
                List<ParticipationRequest> remaining = requestRepository.findByEventIdAndStatus(
                        eventId, RequestStatus.PENDING);
                remaining.stream().filter(request -> !requests.contains(request))
                        .forEach(request -> {
                            request.setStatus(RequestStatus.REJECTED);
                            rejected.add(request);
                        });
                requestRepository.saveAll(remaining);
            }
        }
        eventRepository.save(event);
        requestRepository.saveAll(requests);
        return new EventRequestStatusUpdateResult(confirmed.stream().map(EntityMapper::toRequestDto).toList(),
                rejected.stream().map(EntityMapper::toRequestDto).toList());
    }

    private Event getLockedEvent(Long eventId) {
        return eventRepository.findLockedById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
    }

    private void ensureOwner(Long eventId, Long userId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
    }
}
