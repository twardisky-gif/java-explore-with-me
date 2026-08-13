package ru.practicum.ewm.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import ru.practicum.ewm.model.RequestStatus;

import java.util.Set;

public record EventRequestStatusUpdateDto(
        @NotEmpty
        Set<Long> requestIds,
        @NotNull
        RequestStatus status) {
}
