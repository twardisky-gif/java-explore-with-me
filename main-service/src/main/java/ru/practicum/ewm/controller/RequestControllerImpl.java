package ru.practicum.ewm.controller;

import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.service.RequestService;

import java.util.List;

@RestController
public class RequestControllerImpl implements RequestController {
    private final RequestService requestService;

    public RequestControllerImpl(RequestService requestService) {
        this.requestService = requestService;
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        return requestService.getUserRequests(userId);
    }

    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        return requestService.add(userId, eventId);
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        return requestService.cancel(userId, requestId);
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        return requestService.getEventRequests(userId, eventId);
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestStatuses(Long userId, Long eventId,
                                                                 EventRequestStatusUpdateRequest request) {
        return requestService.updateStatuses(userId, eventId, request);
    }
}
