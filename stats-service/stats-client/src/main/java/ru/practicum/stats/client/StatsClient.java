package ru.practicum.stats.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.stats.dto.EndpointHit;
import ru.practicum.stats.dto.StatsDateFormat;
import ru.practicum.stats.dto.ViewStats;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StatsClient {
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern(StatsDateFormat.DATE_TIME_PATTERN);

    private final String serverUrl;
    private final RestTemplate restTemplate;

    public StatsClient() {
        this("http://localhost:9090");
    }

    public StatsClient(String serverUrl) {
        this(serverUrl, createRestTemplate());
    }

    public StatsClient(String serverUrl, RestTemplate restTemplate) {
        this.serverUrl = stripTrailingSlash(serverUrl);
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<Void> saveHit(EndpointHit hit) {
        return restTemplate.postForEntity(serverUrl + "/hit", hit, Void.class);
    }

    public ResponseEntity<ViewStats[]> getStats(LocalDateTime start, LocalDateTime end) {
        return getStats(start, end, List.of(), false);
    }

    public ResponseEntity<ViewStats[]> getStats(LocalDateTime start, LocalDateTime end,
                                                List<String> uris) {
        return getStats(start, end, uris, false);
    }

    public ResponseEntity<ViewStats[]> getStats(LocalDateTime start, LocalDateTime end,
                                                List<String> uris, boolean unique) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(serverUrl + "/stats")
                .queryParam("start", start.format(DATE_TIME_FORMATTER))
                .queryParam("end", end.format(DATE_TIME_FORMATTER))
                .queryParam("unique", unique);

        if (uris != null) {
            uris.forEach(uri -> builder.queryParam("uris", uri));
        }

        URI requestUri = builder.build().encode().toUri();
        return restTemplate.getForEntity(requestUri, ViewStats[].class);
    }

    private static String stripTrailingSlash(String url) {
        if (url.endsWith("/")) {
            return url.substring(0, url.length() - 1);
        }
        return url;
    }

    private static RestTemplate createRestTemplate() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        RestTemplate template = new RestTemplate();
        template.getMessageConverters().removeIf(MappingJackson2HttpMessageConverter.class::isInstance);
        template.getMessageConverters().add(new MappingJackson2HttpMessageConverter(objectMapper));
        return template;
    }
}
