package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.practicum.stats.dto.StatsDateFormat;

import java.time.LocalDateTime;

public record NewEventDto(
        @NotBlank @Size(min = 20, max = 2000) String annotation,
        @NotNull Long category,
        @NotBlank @Size(min = 20, max = 7000) String description,
        @NotNull @JsonFormat(pattern = StatsDateFormat.DATE_TIME_PATTERN) LocalDateTime eventDate,
        @NotNull @Valid Location location,
        Boolean paid,
        @Min(0) Integer participantLimit,
        Boolean requestModeration,
        @NotBlank @Size(min = 3, max = 120) String title) {
}
