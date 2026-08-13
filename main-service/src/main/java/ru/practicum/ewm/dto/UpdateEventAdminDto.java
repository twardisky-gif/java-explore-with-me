package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import ru.practicum.ewm.model.AdminStateAction;
import ru.practicum.stats.dto.StatsDateFormat;

import java.time.LocalDateTime;

public record UpdateEventAdminDto(
        @Size(min = 20, max = 2000)
        String annotation,
        Long category,
        @Size(min = 20, max = 7000)
        String description,
        @JsonFormat(pattern = StatsDateFormat.DATE_TIME_PATTERN)
        LocalDateTime eventDate,
        @Valid
        LocationDto location,
        Boolean paid,
        @Min(0)
        Integer participantLimit,
        Boolean requestModeration,
        AdminStateAction stateAction,
        @Size(min = 3, max = 120)
        @Pattern(regexp = ".*\\S.*")
        String title) {
}
