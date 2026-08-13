package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.dto.Location;
import ru.practicum.ewm.dto.ParticipationRequestDto;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.dto.UserShortDto;
import ru.practicum.ewm.entity.Category;
import ru.practicum.ewm.entity.Compilation;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.entity.ParticipationRequest;
import ru.practicum.ewm.entity.User;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public final class EntityMapper {
    private EntityMapper() {
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getName());
    }

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public static EventFullDto toEventFullDto(Event event, long views) {
        return new EventFullDto(event.getAnnotation(), toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(), event.getCreatedOn(), event.getDescription(), event.getEventDate(),
                event.getId(), toUserShortDto(event.getInitiator()),
                new Location(event.getLatitude(), event.getLongitude()), event.isPaid(), event.getParticipantLimit(),
                event.getPublishedOn(), event.isRequestModeration(), event.getState(), event.getTitle(), views);
    }

    public static EventShortDto toEventShortDto(Event event, long views) {
        return new EventShortDto(event.getAnnotation(), toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(), event.getEventDate(), event.getId(),
                toUserShortDto(event.getInitiator()), event.isPaid(), event.getTitle(), views);
    }

    public static ParticipationRequestDto toRequestDto(ParticipationRequest request) {
        return new ParticipationRequestDto(request.getCreated(), request.getEvent().getId(), request.getId(),
                request.getRequester().getId(), request.getStatus());
    }

    public static CompilationDto toCompilationDto(Compilation compilation, Map<Long, Long> views) {
        List<EventShortDto> events = compilation.getEvents().stream()
                .sorted(Comparator.comparing(Event::getId))
                .map(event -> toEventShortDto(event, views.getOrDefault(event.getId(), 0L)))
                .toList();
        return new CompilationDto(events, compilation.getId(), compilation.isPinned(), compilation.getTitle());
    }
}
