package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.ewm.model.RequestStatus;
import ru.practicum.stats.dto.StatsDateFormat;

import java.time.LocalDateTime;

public record ParticipationRequestDto(
        @JsonFormat(pattern = StatsDateFormat.DATE_TIME_PATTERN) LocalDateTime created,
        Long event,
        Long id,
        Long requester,
        RequestStatus status) {
}
