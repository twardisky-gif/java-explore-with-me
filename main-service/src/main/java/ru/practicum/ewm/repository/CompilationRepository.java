package ru.practicum.ewm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.entity.Compilation;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    Page<Compilation> findByPinned(boolean pinned, Pageable pageable);

    @EntityGraph(attributePaths = {"events", "events.category", "events.initiator"})
    List<Compilation> findByIdIn(Collection<Long> ids);

    @Override
    @EntityGraph(attributePaths = {"events", "events.category", "events.initiator"})
    Optional<Compilation> findById(Long id);
}
