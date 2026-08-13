package ru.practicum.ewm.dto;

import java.util.List;

public record EventRequestStatusUpdateResultDto(
        List<ParticipationRequestDto> confirmedRequests,
        List<ParticipationRequestDto> rejectedRequests) {
}
