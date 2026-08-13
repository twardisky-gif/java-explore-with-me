package ru.practicum.ewm.controller;

import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.service.CommentService;

@RestController
public class AdminCommentControllerImpl implements AdminCommentController {
    private final CommentService commentService;

    public AdminCommentControllerImpl(CommentService commentService) {
        this.commentService = commentService;
    }

    @Override
    public void deleteComment(Long commentId) {
        commentService.deleteByAdmin(commentId);
    }
}
