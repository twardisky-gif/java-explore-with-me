package ru.practicum.ewm.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.UpdateEventAdminDto;
import ru.practicum.ewm.model.EventState;
import ru.practicum.stats.dto.StatsDateFormat;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.controller.ControllerConstants.DEFAULT_FROM;
import static ru.practicum.ewm.controller.ControllerConstants.DEFAULT_SIZE;
import static ru.practicum.ewm.controller.ControllerConstants.MIN_FROM;

/**
 * Administrative event API.
 */
@Validated
@RequestMapping("/admin/events")
public interface AdminEventController {
    /** Returns events selected by administrative filters. */
    @GetMapping
    List<EventFullDto> getAdminEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<EventState> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = StatsDateFormat.DATE_TIME_PATTERN) LocalDateTime rangeStart,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = StatsDateFormat.DATE_TIME_PATTERN) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = DEFAULT_FROM) @Min(MIN_FROM) int from,
            @RequestParam(defaultValue = DEFAULT_SIZE) @Positive int size);

    /** Updates and moderates an event. */
    @PatchMapping("/{eventId}")
    EventFullDto updateAdminEvent(@PathVariable Long eventId,
                                  @Valid @RequestBody UpdateEventAdminDto request);
}
