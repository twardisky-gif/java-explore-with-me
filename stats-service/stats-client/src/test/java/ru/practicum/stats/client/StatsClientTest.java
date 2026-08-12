package ru.practicum.stats.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import ru.practicum.stats.dto.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withCreatedEntity;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class StatsClientTest {
    private StatsClient client;
    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        server = MockRestServiceServer.bindTo(restTemplate).build();
        client = new StatsClient("http://localhost:9090/", restTemplate);
    }

    @Test
    void shouldSendHit() {
        server.expect(once(), requestTo("http://localhost:9090/hit"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andRespond(withCreatedEntity(null));

        client.saveHit(new EndpointHit(
                null, "main", "/events/1", "127.0.0.1", LocalDateTime.of(2024, 1, 1, 12, 0)));

        server.verify();
    }

    @Test
    void shouldEncodeDatesAndRepeatedUriParameters() {
        server.expect(once(), requestTo("http://localhost:9090/stats?start=2024-01-01%2000:00:00"
                        + "&end=2024-01-02%2000:00:00&unique=true&uris=/events/1&uris=/events/2"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        client.getStats(
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 1, 2, 0, 0),
                List.of("/events/1", "/events/2"),
                true);

        server.verify();
    }
}
