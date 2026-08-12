package ru.practicum.stats.controller;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.stats.dto.EndpointHit;
import ru.practicum.stats.dto.StatsDateFormat;
import ru.practicum.stats.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsController {
    /**
     * Saves information about a request to another service endpoint.
     *
     * @param hit request information
     */
    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    void saveHit(@Valid @RequestBody EndpointHit hit);

    /**
     * Returns view statistics for the requested period.
     *
     * @param start start of the inclusive period
     * @param end end of the inclusive period
     * @param uris optional endpoint filter
     * @param unique whether only unique client IP addresses should be counted
     * @return aggregated statistics sorted by view count
     */
    @GetMapping("/stats")
    List<ViewStats> getStats(
            @RequestParam @DateTimeFormat(pattern = StatsDateFormat.DATE_TIME_PATTERN) LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = StatsDateFormat.DATE_TIME_PATTERN) LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") boolean unique);
}
