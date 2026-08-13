package ru.practicum.ewm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Administrative API for comment moderation.
 */
@RequestMapping("/admin/comments")
public interface AdminCommentController {
    /** Deletes any comment as administrator. */
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteComment(@PathVariable Long commentId);
}
