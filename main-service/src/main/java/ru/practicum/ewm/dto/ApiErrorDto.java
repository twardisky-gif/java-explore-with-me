package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;
import ru.practicum.stats.dto.StatsDateFormat;

import java.time.LocalDateTime;
import java.util.List;

public record ApiErrorDto(
        List<String> errors,
        String message,
        String reason,
        HttpStatus status,
        @JsonFormat(pattern = StatsDateFormat.DATE_TIME_PATTERN)
        LocalDateTime timestamp) {
}
