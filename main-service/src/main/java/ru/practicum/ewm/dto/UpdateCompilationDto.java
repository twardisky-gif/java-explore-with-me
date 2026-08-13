package ru.practicum.ewm.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UpdateCompilationDto(
        Set<Long> events,
        Boolean pinned,
        @Size(max = 50)
        @Pattern(regexp = ".*\\S.*")
        String title) {
}
