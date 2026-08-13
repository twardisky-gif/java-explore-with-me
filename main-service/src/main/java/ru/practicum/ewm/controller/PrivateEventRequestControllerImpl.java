package ru.practicum.ewm.controller;

import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.EventRequestStatusUpdateDto;
import ru.practicum.ewm.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.service.RequestService;

import java.util.List;

@RestController
public class PrivateEventRequestControllerImpl implements PrivateEventRequestController {
    private final RequestService requestService;

    public PrivateEventRequestControllerImpl(RequestService requestService) {
        this.requestService = requestService;
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        return requestService.getEventRequests(userId, eventId);
    }

    @Override
    public EventRequestStatusUpdateResultDto updateRequestStatuses(Long userId, Long eventId,
                                                                    EventRequestStatusUpdateDto request) {
        return requestService.updateStatuses(userId, eventId, request);
    }
}
