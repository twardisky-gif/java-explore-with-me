package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.stats.dto.StatsDateFormat;

import java.time.LocalDateTime;

public record EventShortDto(
        String annotation,
        CategoryDto category,
        long confirmedRequests,
        @JsonFormat(pattern = StatsDateFormat.DATE_TIME_PATTERN) LocalDateTime eventDate,
        Long id,
        UserShortDto initiator,
        boolean paid,
        String title,
        long views) {
}
