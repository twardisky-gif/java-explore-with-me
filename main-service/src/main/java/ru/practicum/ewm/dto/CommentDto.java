package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.stats.dto.StatsDateFormat;

import java.time.LocalDateTime;

public record CommentDto(
        Long id,
        String text,
        UserShortDto author,
        Long eventId,
        @JsonFormat(pattern = StatsDateFormat.DATE_TIME_PATTERN)
        LocalDateTime created,
        @JsonFormat(pattern = StatsDateFormat.DATE_TIME_PATTERN)
        LocalDateTime updated) {
}
