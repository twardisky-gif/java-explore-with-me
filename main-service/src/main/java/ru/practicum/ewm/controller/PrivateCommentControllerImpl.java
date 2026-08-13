package ru.practicum.ewm.controller;

import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.CommentDto;
import ru.practicum.ewm.dto.NewCommentDto;
import ru.practicum.ewm.dto.UpdateCommentDto;
import ru.practicum.ewm.service.CommentService;

import java.util.List;

@RestController
public class PrivateCommentControllerImpl implements PrivateCommentController {
    private final CommentService commentService;

    public PrivateCommentControllerImpl(CommentService commentService) {
        this.commentService = commentService;
    }

    @Override
    public CommentDto createComment(Long userId, Long eventId, NewCommentDto request) {
        return commentService.create(userId, eventId, request);
    }

    @Override
    public CommentDto updateComment(Long userId, Long commentId, UpdateCommentDto request) {
        return commentService.update(userId, commentId, request);
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        commentService.deleteByAuthor(userId, commentId);
    }

    @Override
    public List<CommentDto> getUserComments(Long userId, int from, int size) {
        return commentService.getByAuthor(userId, from, size);
    }
}
