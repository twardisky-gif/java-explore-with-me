package ru.practicum.ewm.controller;

import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.CommentDto;
import ru.practicum.ewm.service.CommentService;

import java.util.List;

@RestController
public class PublicCommentControllerImpl implements PublicCommentController {
    private final CommentService commentService;

    public PublicCommentControllerImpl(CommentService commentService) {
        this.commentService = commentService;
    }

    @Override
    public CommentDto getComment(Long commentId) {
        return commentService.get(commentId);
    }

    @Override
    public List<CommentDto> getEventComments(Long eventId, int from, int size) {
        return commentService.getByEvent(eventId, from, size);
    }
}
