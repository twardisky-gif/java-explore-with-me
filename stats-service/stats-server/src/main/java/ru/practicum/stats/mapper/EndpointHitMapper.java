package ru.practicum.stats.mapper;

import ru.practicum.stats.dto.EndpointHit;
import ru.practicum.stats.entity.EndpointHitEntity;

public final class EndpointHitMapper {
    private EndpointHitMapper() {
    }

    public static EndpointHitEntity toEntity(EndpointHit hit) {
        return new EndpointHitEntity(null, hit.getApp(), hit.getUri(), hit.getIp(), hit.getTimestamp());
    }
}
