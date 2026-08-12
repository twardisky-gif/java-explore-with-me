package ru.practicum.stats.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats.dto.EndpointHit;
import ru.practicum.stats.dto.ViewStats;
import ru.practicum.stats.exception.InvalidDateRangeException;
import ru.practicum.stats.mapper.EndpointHitMapper;
import ru.practicum.stats.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repository;

    public StatsServiceImpl(StatsRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void saveHit(EndpointHit hit) {
        repository.save(EndpointHitMapper.toEntity(hit));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end,
                                    List<String> uris, boolean unique) {
        if (start.isAfter(end)) {
            throw new InvalidDateRangeException("Start date must not be after end date");
        }

        boolean allUris = uris == null || uris.isEmpty();
        if (allUris) {
            return unique ? repository.findUniqueStats(start, end) : repository.findStats(start, end);
        }
        return unique
                ? repository.findUniqueStatsByUris(start, end, uris)
                : repository.findStatsByUris(start, end, uris);
    }
}
