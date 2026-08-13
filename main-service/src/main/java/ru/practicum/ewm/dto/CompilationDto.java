package ru.practicum.ewm.dto;

import java.util.List;

public record CompilationDto(List<EventShortDto> events, Long id, boolean pinned, String title) {
}
