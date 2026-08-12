package ru.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.stats.dto.ViewStats;
import ru.practicum.stats.entity.EndpointHitEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHitEntity, Long> {
    @Query("""
            select new ru.practicum.stats.dto.ViewStats(hit.app, hit.uri, count(hit.id))
            from EndpointHitEntity hit
            where hit.timestamp between :start and :end
            group by hit.app, hit.uri
            order by count(hit.id) desc
            """)
    List<ViewStats> findStats(@Param("start") LocalDateTime start,
                              @Param("end") LocalDateTime end);

    @Query("""
            select new ru.practicum.stats.dto.ViewStats(hit.app, hit.uri, count(distinct hit.ip))
            from EndpointHitEntity hit
            where hit.timestamp between :start and :end
            group by hit.app, hit.uri
            order by count(distinct hit.ip) desc
            """)
    List<ViewStats> findUniqueStats(@Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end);

    @Query("""
            select new ru.practicum.stats.dto.ViewStats(hit.app, hit.uri, count(hit.id))
            from EndpointHitEntity hit
            where hit.timestamp between :start and :end and hit.uri in :uris
            group by hit.app, hit.uri
            order by count(hit.id) desc
            """)
    List<ViewStats> findStatsByUris(@Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end,
                                    @Param("uris") List<String> uris);

    @Query("""
            select new ru.practicum.stats.dto.ViewStats(hit.app, hit.uri, count(distinct hit.ip))
            from EndpointHitEntity hit
            where hit.timestamp between :start and :end and hit.uri in :uris
            group by hit.app, hit.uri
            order by count(distinct hit.ip) desc
            """)
    List<ViewStats> findUniqueStatsByUris(@Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end,
                                          @Param("uris") List<String> uris);
}
