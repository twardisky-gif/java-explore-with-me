package ru.practicum.ewm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record NewCompilationDto(
        Set<Long> events,
        Boolean pinned,
        @NotBlank @Size(max = 50) String title) {
}
