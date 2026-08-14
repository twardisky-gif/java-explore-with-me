package ru.practicum.ewm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.entity.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @EntityGraph(attributePaths = {"author", "event"})
    Page<Comment> findByEventId(Long eventId, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "event"})
    Page<Comment> findByAuthorId(Long authorId, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"author", "event"})
    Optional<Comment> findById(Long id);

    @EntityGraph(attributePaths = {"author", "event"})
    Optional<Comment> findByIdAndAuthorId(Long id, Long authorId);
}
