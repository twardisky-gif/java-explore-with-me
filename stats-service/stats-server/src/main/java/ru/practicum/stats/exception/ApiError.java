package ru.practicum.stats.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.stats.dto.StatsDateFormat;

import java.time.LocalDateTime;

public record ApiError(
        @JsonFormat(pattern = StatsDateFormat.DATE_TIME_PATTERN) LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path) {
}
