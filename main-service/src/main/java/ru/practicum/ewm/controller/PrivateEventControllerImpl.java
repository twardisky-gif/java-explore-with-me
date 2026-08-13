package ru.practicum.ewm.controller;

import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.dto.NewEventDto;
import ru.practicum.ewm.dto.UpdateEventUserDto;
import ru.practicum.ewm.service.EventService;

import java.util.List;

@RestController
public class PrivateEventControllerImpl implements PrivateEventController {
    private final EventService eventService;

    public PrivateEventControllerImpl(EventService eventService) {
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
    public EventFullDto updateUserEvent(Long userId, Long eventId, UpdateEventUserDto request) {
        return eventService.updateByUser(userId, eventId, request);
    }

}
