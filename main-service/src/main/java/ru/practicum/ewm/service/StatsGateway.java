package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import ru.practicum.ewm.entity.Event;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.EndpointHit;
import ru.practicum.stats.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsGateway {
    private static final String APP_NAME = "ewm-main-service";
    private final StatsClient statsClient;

    public StatsGateway(@Value("${stats-server.url}") String statsServerUrl) {
        this.statsClient = new StatsClient(statsServerUrl);
    }

    public void recordHit(String uri, String ip) {
        statsClient.saveHit(new EndpointHit(null, APP_NAME, uri, ip, LocalDateTime.now()));
    }

    public Map<Long, Long> loadViews(Collection<Event> events) {
        if (events.isEmpty()) {
            return Map.of();
        }
        LocalDateTime start = events.stream().map(Event::getCreatedOn).min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());
        List<String> uris = events.stream().map(event -> "/events/" + event.getId()).distinct().toList();
        try {
            ViewStats[] stats = statsClient.getStats(start, LocalDateTime.now(), uris, true).getBody();
            if (stats == null) {
                return Map.of();
            }
            Map<Long, Long> views = new HashMap<>();
            for (ViewStats view : stats) {
                Long id = parseEventId(view.getUri());
                if (id != null) {
                    views.put(id, view.getHits());
                }
            }
            return views;
        } catch (RestClientException exception) {
            return Map.of();
        }
    }

    private Long parseEventId(String uri) {
        String prefix = "/events/";
        if (!uri.startsWith(prefix)) {
            return null;
        }
        try {
            return Long.valueOf(uri.substring(prefix.length()));
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}
