package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.entity.ParticipationRequest;
import ru.practicum.ewm.model.RequestStatus;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {
    @EntityGraph(attributePaths = {"event", "requester"})
    List<ParticipationRequest> findByRequesterId(Long requesterId);

    @EntityGraph(attributePaths = {"event", "requester"})
    List<ParticipationRequest> findByEventId(Long eventId);

    @EntityGraph(attributePaths = {"event", "requester"})
    List<ParticipationRequest> findByEventIdAndStatus(Long eventId, RequestStatus status);

    @EntityGraph(attributePaths = {"event", "requester"})
    List<ParticipationRequest> findByIdInAndEventId(Collection<Long> ids, Long eventId);

    @EntityGraph(attributePaths = {"event", "requester"})
    Optional<ParticipationRequest> findByIdAndRequesterId(Long id, Long requesterId);

    boolean existsByEventIdAndRequesterId(Long eventId, Long requesterId);
}
