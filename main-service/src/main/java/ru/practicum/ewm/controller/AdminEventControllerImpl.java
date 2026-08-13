package ru.practicum.ewm.controller;

import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.UpdateEventAdminDto;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class AdminEventControllerImpl implements AdminEventController {
    private final EventService eventService;

    public AdminEventControllerImpl(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public List<EventFullDto> getAdminEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                              LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                              int from, int size) {
        return eventService.getAdminEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @Override
    public EventFullDto updateAdminEvent(Long eventId, UpdateEventAdminDto request) {
        return eventService.updateByAdmin(eventId, request);
    }
}
