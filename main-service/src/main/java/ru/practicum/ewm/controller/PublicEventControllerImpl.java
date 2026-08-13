package ru.practicum.ewm.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.model.EventSort;
import ru.practicum.ewm.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class PublicEventControllerImpl implements PublicEventController {
    private final EventService eventService;

    public PublicEventControllerImpl(EventService eventService) {
        this.eventService = eventService;
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
