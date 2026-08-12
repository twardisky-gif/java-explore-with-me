package ru.practicum.stats.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.stats.exception.InvalidDateRangeException;
import ru.practicum.stats.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatsController.class)
class StatsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatsService service;

    @Test
    void shouldCreateValidHit() throws Exception {
        mockMvc.perform(post("/hit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":42,\"app\":\"main\",\"uri\":\"/events/1\","
                                + "\"ip\":\"192.168.1.1\",\"timestamp\":\"2024-01-01 12:00:00\"}"))
                .andExpect(status().isCreated());

        verify(service).saveHit(any());
    }

    @Test
    void shouldRejectBlankHitFields() throws Exception {
        mockMvc.perform(post("/hit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"app\":\" \",\"uri\":\"/events/1\",\"ip\":\"\",\"timestamp\":null}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void shouldPassMultipleUrisAndUniqueFlag() throws Exception {
        mockMvc.perform(get("/stats")
                        .param("start", "2024-01-01 00:00:00")
                        .param("end", "2024-01-02 00:00:00")
                        .param("uris", "/events/1", "/events/2")
                        .param("unique", "true"))
                .andExpect(status().isOk());

        verify(service).getStats(
                eq(LocalDateTime.of(2024, 1, 1, 0, 0)),
                eq(LocalDateTime.of(2024, 1, 2, 0, 0)),
                eq(List.of("/events/1", "/events/2")),
                eq(true));
    }

    @Test
    void shouldReturnBadRequestForInvalidRange() throws Exception {
        doThrow(new InvalidDateRangeException("invalid range"))
                .when(service).getStats(any(), any(), any(), anyBoolean());

        mockMvc.perform(get("/stats")
                        .param("start", "2024-01-02 00:00:00")
                        .param("end", "2024-01-01 00:00:00"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("invalid range"));
    }
}
