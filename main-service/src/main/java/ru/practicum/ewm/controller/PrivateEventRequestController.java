package ru.practicum.ewm.controller;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.ewm.dto.EventRequestStatusUpdateDto;
import ru.practicum.ewm.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.ewm.dto.ParticipationRequestDto;

import java.util.List;

/**
 * Private API for participation requests received by an event initiator.
 */
@Validated
@RequestMapping("/users/{userId}/events/{eventId}/requests")
public interface PrivateEventRequestController {
    /** Returns participation requests for an event owned by a user. */
    @GetMapping
    List<ParticipationRequestDto> getEventRequests(@PathVariable Long userId, @PathVariable Long eventId);

    /** Moderates participation requests for an event. */
    @PatchMapping
    EventRequestStatusUpdateResultDto updateRequestStatuses(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Valid @RequestBody EventRequestStatusUpdateDto request);
}
