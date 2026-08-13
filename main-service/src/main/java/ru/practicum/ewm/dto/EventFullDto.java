package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.ewm.model.EventState;
import ru.practicum.stats.dto.StatsDateFormat;

import java.time.LocalDateTime;

public record EventFullDto(
        String annotation,
        CategoryDto category,
        long confirmedRequests,
        @JsonFormat(pattern = StatsDateFormat.DATE_TIME_PATTERN)
        LocalDateTime createdOn,
        String description,
        @JsonFormat(pattern = StatsDateFormat.DATE_TIME_PATTERN)
        LocalDateTime eventDate,
        Long id,
        UserShortDto initiator,
        LocationDto location,
        boolean paid,
        int participantLimit,
        @JsonFormat(pattern = StatsDateFormat.DATE_TIME_PATTERN)
        LocalDateTime publishedOn,
        boolean requestModeration,
        EventState state,
        String title,
        long views) {
}
