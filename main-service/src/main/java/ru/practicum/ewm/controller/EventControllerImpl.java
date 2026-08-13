package ru.practicum.ewm.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.dto.NewEventDto;
import ru.practicum.ewm.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.dto.UpdateEventUserRequest;
import ru.practicum.ewm.model.EventSort;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class EventControllerImpl implements EventController {
    private final EventService eventService;

    public EventControllerImpl(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto request) {
        return eventService.create(userId, request);
    }

    @Override
    public List<EventShortDto> getUserEvents(Long userId, int from, int size) {
        return eventService.getUserEvents(userId, from, size);
    }

    @Override
    public EventFullDto getUserEvent(Long userId, Long eventId) {
        return eventService.getUserEvent(userId, eventId);
    }

    @Override
    public EventFullDto updateUserEvent(Long userId, Long eventId, UpdateEventUserRequest request) {
        return eventService.updateByUser(userId, eventId, request);
    }

    @Override
    public List<EventFullDto> getAdminEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                              LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                              int from, int size) {
        return eventService.getAdminEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @Override
    public EventFullDto updateAdminEvent(Long eventId, UpdateEventAdminRequest request) {
        return eventService.updateByAdmin(eventId, request);
    }

    @Override
    public List<EventShortDto> getPublicEvents(String text, List<Long> categories, Boolean paid,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                boolean onlyAvailable, EventSort sort, int from, int size,
                                                HttpServletRequest request) {
        return eventService.getPublicEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                from, size, request.getRequestURI(), request.getRemoteAddr());
    }

    @Override
    public EventFullDto getPublicEvent(Long eventId, HttpServletRequest request) {
        return eventService.getPublicEvent(eventId, request.getRequestURI(), request.getRemoteAddr());
    }
}
