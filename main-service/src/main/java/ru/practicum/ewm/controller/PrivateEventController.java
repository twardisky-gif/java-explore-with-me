package ru.practicum.ewm.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.dto.NewEventDto;
import ru.practicum.ewm.dto.UpdateEventUserDto;

import java.util.List;

import static ru.practicum.ewm.controller.ControllerConstants.DEFAULT_FROM;
import static ru.practicum.ewm.controller.ControllerConstants.DEFAULT_SIZE;
import static ru.practicum.ewm.controller.ControllerConstants.MIN_FROM;

/**
 * Private event API for event initiators.
 */
@Validated
@RequestMapping("/users/{userId}/events")
public interface PrivateEventController {
    /** Creates an event for a user. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    EventFullDto createEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto request);

    /** Returns events created by a user. */
    @GetMapping
    List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                      @RequestParam(defaultValue = DEFAULT_FROM) @Min(MIN_FROM) int from,
                                      @RequestParam(defaultValue = DEFAULT_SIZE) @Positive int size);

    /** Returns one event created by a user. */
    @GetMapping("/{eventId}")
    EventFullDto getUserEvent(@PathVariable Long userId, @PathVariable Long eventId);

    /** Updates an event by its initiator. */
    @PatchMapping("/{eventId}")
    EventFullDto updateUserEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                 @Valid @RequestBody UpdateEventUserDto request);
}
