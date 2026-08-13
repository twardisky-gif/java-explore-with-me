package ru.practicum.ewm.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.model.EventSort;
import ru.practicum.stats.dto.StatsDateFormat;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.controller.ControllerConstants.DEFAULT_FROM;
import static ru.practicum.ewm.controller.ControllerConstants.DEFAULT_SIZE;
import static ru.practicum.ewm.controller.ControllerConstants.MIN_FROM;

/**
 * Public event API.
 */
@Validated
@RequestMapping("/events")
public interface PublicEventController {
    /** Returns published events selected by public filters. */
    @GetMapping
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
            @RequestParam(defaultValue = DEFAULT_FROM) @Min(MIN_FROM) int from,
            @RequestParam(defaultValue = DEFAULT_SIZE) @Positive int size,
            HttpServletRequest request);

    /** Returns a published event and records its view. */
    @GetMapping("/{eventId}")
    EventFullDto getPublicEvent(@PathVariable Long eventId, HttpServletRequest request);
}
