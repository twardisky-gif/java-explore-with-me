package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.CommentDto;
import ru.practicum.ewm.dto.NewCommentDto;
import ru.practicum.ewm.dto.UpdateCommentDto;
import java.util.List;

/**
 * Provides comment management operations.
 */
public interface CommentService {
    /** Creates a comment for a published event. */
    CommentDto create(Long userId, Long eventId, NewCommentDto request);

    /** Updates a comment owned by a user. */
    CommentDto update(Long userId, Long commentId, UpdateCommentDto request);

    /** Deletes a comment owned by a user. */
    void deleteByAuthor(Long userId, Long commentId);

    /** Deletes any comment as administrator. */
    void deleteByAdmin(Long commentId);

    /** Returns a comment by identifier. */
    CommentDto get(Long commentId);

    /** Returns comments for an event. */
    List<CommentDto> getByEvent(Long eventId, int from, int size);

    /** Returns comments written by a user. */
    List<CommentDto> getByAuthor(Long userId, int from, int size);
}
