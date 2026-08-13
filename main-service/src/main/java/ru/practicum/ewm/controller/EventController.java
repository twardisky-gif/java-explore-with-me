package ru.practicum.ewm.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.dto.NewEventDto;
import ru.practicum.ewm.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.dto.UpdateEventUserRequest;
import ru.practicum.ewm.model.EventSort;
import ru.practicum.ewm.model.EventState;
import ru.practicum.stats.dto.StatsDateFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Public, private and administrative event operations.
 */
@Validated
public interface EventController {
    /** Creates an event for a user. */
    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    EventFullDto createEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto request);

    /** Returns events created by a user. */
    @GetMapping("/users/{userId}/events")
    List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                      @RequestParam(defaultValue = "0") @Min(0) int from,
                                      @RequestParam(defaultValue = "10") @Positive int size);

    /** Returns one event created by a user. */
    @GetMapping("/users/{userId}/events/{eventId}")
    EventFullDto getUserEvent(@PathVariable Long userId, @PathVariable Long eventId);

    /** Updates an event by its initiator. */
    @PatchMapping("/users/{userId}/events/{eventId}")
    EventFullDto updateUserEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                 @Valid @RequestBody UpdateEventUserRequest request);

    /** Returns events selected by administrative filters. */
    @GetMapping("/admin/events")
    List<EventFullDto> getAdminEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<EventState> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = StatsDateFormat.DATE_TIME_PATTERN) LocalDateTime rangeStart,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = StatsDateFormat.DATE_TIME_PATTERN) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") @Min(0) int from,
            @RequestParam(defaultValue = "10") @Positive int size);

    /** Updates and moderates an event as administrator. */
    @PatchMapping("/admin/events/{eventId}")
    EventFullDto updateAdminEvent(@PathVariable Long eventId,
                                  @Valid @RequestBody UpdateEventAdminRequest request);

    /** Returns published events selected by public filters. */
    @GetMapping("/events")
    List<EventShortDto> getPublicEvents(
            @RequestParam(required = false) @Size(min = 1, max = 7000) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = StatsDateFormat.DATE_TIME_PATTERN) LocalDateTime rangeStart,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = StatsDateFormat.DATE_TIME_PATTERN) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") boolean onlyAvailable,
            @RequestParam(required = false) EventSort sort,
            @RequestParam(defaultValue = "0") @Min(0) int from,
            @RequestParam(defaultValue = "10") @Positive int size,
            HttpServletRequest request);

    /** Returns a published event and records its view. */
    @GetMapping("/events/{eventId}")
    EventFullDto getPublicEvent(@PathVariable("eventId") Long eventId, HttpServletRequest request);
}
