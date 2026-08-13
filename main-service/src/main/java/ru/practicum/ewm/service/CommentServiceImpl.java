package ru.practicum.ewm.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.CommentDto;
import ru.practicum.ewm.dto.NewCommentDto;
import ru.practicum.ewm.dto.UpdateCommentDto;
import ru.practicum.ewm.dto.UserShortDto;
import ru.practicum.ewm.entity.Comment;
import ru.practicum.ewm.entity.Event;
import ru.practicum.ewm.entity.User;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.repository.CommentRepository;
import ru.practicum.ewm.repository.OffsetPageRequest;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private static final Sort COMMENT_SORT = Sort.by(Sort.Direction.DESC, "created");
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final EventService eventService;

    public CommentServiceImpl(CommentRepository commentRepository, UserService userService,
                              EventService eventService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.eventService = eventService;
    }

    @Override
    @Transactional
    public CommentDto create(Long userId, Long eventId, NewCommentDto request) {
        User author = userService.getEntity(userId);
        Event event = eventService.getEvent(eventId);
        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Comments can be added only to published events");
        }
        LocalDateTime now = LocalDateTime.now();
        Comment comment = new Comment();
        comment.setText(request.text());
        comment.setAuthor(author);
        comment.setEvent(event);
        comment.setCreated(now);
        comment.setUpdated(now);
        return toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDto update(Long userId, Long commentId, UpdateCommentDto request) {
        userService.getEntity(userId);
        Comment comment = getOwnedComment(commentId, userId);
        comment.setText(request.text());
        comment.setUpdated(LocalDateTime.now());
        return toDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteByAuthor(Long userId, Long commentId) {
        userService.getEntity(userId);
        commentRepository.delete(getOwnedComment(commentId, userId));
    }

    @Override
    @Transactional
    public void deleteByAdmin(Long commentId) {
        commentRepository.delete(getComment(commentId));
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto get(Long commentId) {
        return toDto(getComment(commentId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getByEvent(Long eventId, int from, int size) {
        Event event = eventService.getEvent(eventId);
        if (event.getState() != EventState.PUBLISHED) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
        return commentRepository.findByEventId(eventId, new OffsetPageRequest(from, size, COMMENT_SORT))
                .stream().map(CommentServiceImpl::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getByAuthor(Long userId, int from, int size) {
        userService.getEntity(userId);
        return commentRepository.findByAuthorId(userId, new OffsetPageRequest(from, size, COMMENT_SORT))
                .stream().map(CommentServiceImpl::toDto).toList();
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found"));
    }

    private Comment getOwnedComment(Long commentId, Long userId) {
        return commentRepository.findByIdAndAuthorId(commentId, userId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found"));
    }

    private static CommentDto toDto(Comment comment) {
        User author = comment.getAuthor();
        return new CommentDto(comment.getId(), comment.getText(), new UserShortDto(author.getId(), author.getName()),
                comment.getEvent().getId(), comment.getCreated(), comment.getUpdated());
    }
}
