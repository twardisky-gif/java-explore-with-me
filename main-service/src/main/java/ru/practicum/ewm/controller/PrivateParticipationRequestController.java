package ru.practicum.ewm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.ewm.dto.ParticipationRequestDto;

import java.util.List;

/**
 * Private participation request operations.
 */
@Validated
@RequestMapping("/users/{userId}/requests")
public interface PrivateParticipationRequestController {
    /** Returns requests submitted by a user. */
    @GetMapping
    List<ParticipationRequestDto> getUserRequests(@PathVariable Long userId);

    /** Creates a participation request. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ParticipationRequestDto createRequest(@PathVariable Long userId, @RequestParam Long eventId);

    /** Cancels a request submitted by a user. */
    @PatchMapping("/{requestId}/cancel")
    ParticipationRequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId);
}
