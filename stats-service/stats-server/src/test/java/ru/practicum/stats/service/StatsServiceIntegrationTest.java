package ru.practicum.stats.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.practicum.stats.dto.EndpointHit;
import ru.practicum.stats.dto.ViewStats;
import ru.practicum.stats.exception.InvalidDateRangeException;
import ru.practicum.stats.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.sql.init.mode=never"
})
@Import(StatsServiceImpl.class)
class StatsServiceIntegrationTest {
    private static final LocalDateTime START = LocalDateTime.of(2024, 1, 1, 0, 0);
    private static final LocalDateTime END = LocalDateTime.of(2024, 1, 2, 0, 0);

    @Autowired
    private StatsService service;

    @Autowired
    private StatsRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void shouldSaveHitAndIgnoreIncomingId() {
        service.saveHit(new EndpointHit(999L, "main", "/events/1", "10.0.0.1", START));

        assertThat(repository.findAll())
                .singleElement()
                .satisfies(hit -> {
                    assertThat(hit.getId()).isNotEqualTo(999L);
                    assertThat(hit.getUri()).isEqualTo("/events/1");
                });
    }

    @Test
    void shouldReturnStatsSortedByHits() {
        save("/events/1", "10.0.0.1");
        save("/events/2", "10.0.0.2");
        save("/events/2", "10.0.0.3");

        List<ViewStats> stats = service.getStats(START.minusSeconds(1), END, null, false);

        assertThat(stats).containsExactly(
                new ViewStats("main", "/events/2", 2L),
                new ViewStats("main", "/events/1", 1L)
        );
    }

    @Test
    void shouldUseUriAsStableOrderWhenHitsAreEqual() {
        save("/events/2", "10.0.0.2");
        save("/events/1", "10.0.0.1");

        List<ViewStats> stats = service.getStats(START.minusSeconds(1), END, null, false);

        assertThat(stats)
                .extracting(ViewStats::getUri)
                .containsExactly("/events/1", "/events/2");
    }

    @Test
    void shouldFilterBySeveralUris() {
        save("/events/1", "10.0.0.1");
        save("/events/2", "10.0.0.2");
        save("/events/3", "10.0.0.3");

        List<ViewStats> stats = service.getStats(
                START.minusSeconds(1), END, List.of("/events/1", "/events/3"), false);

        assertThat(stats)
                .extracting(ViewStats::getUri)
                .containsExactlyInAnyOrder("/events/1", "/events/3");
    }

    @Test
    void shouldCountUniqueIpsPerAppAndUri() {
        save("/events/1", "10.0.0.1");
        save("/events/1", "10.0.0.1");
        save("/events/1", "10.0.0.2");

        List<ViewStats> stats = service.getStats(
                START.minusSeconds(1), END, List.of("/events/1"), true);

        assertThat(stats).containsExactly(new ViewStats("main", "/events/1", 2L));
    }

    @Test
    void shouldReturnEmptyListWhenNothingMatches() {
        assertThat(service.getStats(START, END, List.of(), false)).isEmpty();
    }

    @Test
    void shouldRejectReversedDateRange() {
        assertThatThrownBy(() -> service.getStats(END, START, null, false))
                .isInstanceOf(InvalidDateRangeException.class);
    }

    private void save(String uri, String ip) {
        service.saveHit(new EndpointHit(null, "main", uri, ip, START.plusHours(1)));
    }
}
