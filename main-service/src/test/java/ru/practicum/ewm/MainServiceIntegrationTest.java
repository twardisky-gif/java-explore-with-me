package ru.practicum.ewm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.service.StatsGateway;
import ru.practicum.stats.dto.StatsDateFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MainServiceIntegrationTest {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern(StatsDateFormat.DATE_TIME_PATTERN);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StatsGateway statsGateway;

    @BeforeEach
    void setUp() {
        when(statsGateway.loadViews(anyCollection())).thenReturn(Map.of());
    }

    @Test
    void shouldCreatePublishAndReadEvent() throws Exception {
        long ownerId = createUser("owner@example.com", "Owner");
        long requesterId = createUser("requester@example.com", "Requester");
        long categoryId = createCategory("Concerts");
        String eventDate = LocalDateTime.now().plusDays(1).format(FORMATTER);

        String eventResponse = mockMvc.perform(post("/users/{userId}/events", ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson(categoryId, eventDate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.state", is("PENDING")))
                .andExpect(jsonPath("$.paid", is(false)))
                .andExpect(jsonPath("$.participantLimit", is(0)))
                .andExpect(jsonPath("$.requestModeration", is(true)))
                .andReturn().getResponse().getContentAsString();
        long eventId = objectMapper.readTree(eventResponse).get("id").asLong();

        mockMvc.perform(patch("/admin/events/{eventId}", eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stateAction\":\"PUBLISH_EVENT\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state", is("PUBLISHED")));

        mockMvc.perform(get("/events/{eventId}", eventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) eventId)));

        mockMvc.perform(post("/users/{userId}/requests", requesterId).param("eventId", String.valueOf(eventId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is("CONFIRMED")));
    }

    @Test
    void shouldRejectEventInThePast() throws Exception {
        long userId = createUser("past@example.com", "Past User");
        long categoryId = createCategory("Lectures");
        String eventDate = LocalDateTime.now().minusHours(1).format(FORMATTER);

        mockMvc.perform(post("/users/{userId}/events", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson(categoryId, eventDate)))
                .andExpect(status().isBadRequest());
    }

    private long createUser(String email, String name) throws Exception {
        String response = mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"name\":\"" + name + "\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).get("id").asLong();
    }

    private long createCategory(String name) throws Exception {
        String response = mockMvc.perform(post("/admin/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + name + "\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        JsonNode body = objectMapper.readTree(response);
        return body.get("id").asLong();
    }

    private String eventJson(long categoryId, String eventDate) {
        return "{\"annotation\":\"A sufficiently long event annotation\","
                + "\"category\":" + categoryId + ","
                + "\"description\":\"A sufficiently long event description\","
                + "\"eventDate\":\"" + eventDate + "\","
                + "\"location\":{\"lat\":55.75,\"lon\":37.61},"
                + "\"title\":\"Integration event\"}";
    }
}
