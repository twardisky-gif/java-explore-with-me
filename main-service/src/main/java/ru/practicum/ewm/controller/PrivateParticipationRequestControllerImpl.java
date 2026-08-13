package ru.practicum.ewm.controller;

import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.service.RequestService;

import java.util.List;

@RestController
public class PrivateParticipationRequestControllerImpl implements PrivateParticipationRequestController {
    private final RequestService requestService;

    public PrivateParticipationRequestControllerImpl(RequestService requestService) {
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

}
