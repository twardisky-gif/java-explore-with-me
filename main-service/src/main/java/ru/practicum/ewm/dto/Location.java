package ru.practicum.ewm.dto;

import jakarta.validation.constraints.NotNull;

public record Location(@NotNull Float lat, @NotNull Float lon) {
}
