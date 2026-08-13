package ru.practicum.ewm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.dto.UpdateCompilationDto;
import ru.practicum.ewm.entity.Compilation;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.EntityMapper;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.OffsetPageRequest;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class CompilationService {
    private static final int FIRST_POSITION = 0;
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final StatsGateway statsGateway;

    public CompilationService(CompilationRepository compilationRepository, EventRepository eventRepository,
                              StatsGateway statsGateway) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
        this.statsGateway = statsGateway;
    }

    @Transactional
    public CompilationDto create(NewCompilationDto request) {
        Compilation compilation = new Compilation();
        compilation.setTitle(request.title());
        compilation.setPinned(Boolean.TRUE.equals(request.pinned()));
        compilation.setEvents(loadEvents(request.events()));
        Compilation saved = compilationRepository.save(compilation);
        return EntityMapper.toCompilationDto(saved, statsGateway.loadViews(saved.getEvents()));
    }

    @Transactional
    public CompilationDto update(Long compilationId, UpdateCompilationDto request) {
        Compilation compilation = getEntity(compilationId);
        if (request.title() != null) {
            compilation.setTitle(request.title());
        }
        if (request.pinned() != null) {
            compilation.setPinned(request.pinned());
        }
        if (request.events() != null) {
            compilation.setEvents(loadEvents(request.events()));
        }
        return EntityMapper.toCompilationDto(compilationRepository.save(compilation),
                statsGateway.loadViews(compilation.getEvents()));
    }

    @Transactional
    public void delete(Long compilationId) {
        compilationRepository.delete(getEntity(compilationId));
    }

    @Transactional(readOnly = true)
    public CompilationDto get(Long compilationId) {
        Compilation compilation = getEntity(compilationId);
        return EntityMapper.toCompilationDto(compilation, statsGateway.loadViews(compilation.getEvents()));
    }

    @Transactional(readOnly = true)
    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        OffsetPageRequest pageable = new OffsetPageRequest(from, size, Sort.by("id"));
        Page<Compilation> page = pinned == null
                ? compilationRepository.findAll(pageable)
                : compilationRepository.findByPinned(pinned, pageable);
        List<Long> compilationIds = page.stream().map(Compilation::getId).toList();
        if (compilationIds.isEmpty()) {
            return List.of();
        }
        List<Compilation> compilations = compilationRepository.findByIdIn(compilationIds);
        Map<Long, Integer> positions = new HashMap<>();
        for (int index = FIRST_POSITION; index < compilationIds.size(); index++) {
            positions.put(compilationIds.get(index), index);
        }
        compilations.sort(Comparator.comparingInt(compilation -> positions.get(compilation.getId())));
        List<Event> events = compilations.stream().map(Compilation::getEvents)
                .flatMap(Collection::stream).distinct().toList();
        Map<Long, Long> views = statsGateway.loadViews(events);
        return compilations.stream().map(compilation -> EntityMapper.toCompilationDto(compilation, views)).toList();
    }

    private Compilation getEntity(Long compilationId) {
        return compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compilationId + " was not found"));
    }

    private Set<Event> loadEvents(Set<Long> eventIds) {
        if (eventIds == null || eventIds.isEmpty()) {
            return new LinkedHashSet<>();
        }
        List<Event> events = eventRepository.findByIdIn(eventIds);
        if (events.size() != eventIds.size()) {
            throw new NotFoundException("One or more events were not found");
        }
        return new LinkedHashSet<>(events);
    }
}
