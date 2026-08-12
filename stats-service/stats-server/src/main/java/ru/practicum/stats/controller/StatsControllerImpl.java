package ru.practicum.stats.controller;

import org.springframework.web.bind.annotation.RestController;
import ru.practicum.stats.dto.EndpointHit;
import ru.practicum.stats.dto.ViewStats;
import ru.practicum.stats.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class StatsControllerImpl implements StatsController {
    private final StatsService service;

    public StatsControllerImpl(StatsService service) {
        this.service = service;
    }

    @Override
    public void saveHit(EndpointHit hit) {
        service.saveHit(hit);
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end,
                                    List<String> uris, boolean unique) {
        return service.getStats(start, end, uris, unique);
    }
}
