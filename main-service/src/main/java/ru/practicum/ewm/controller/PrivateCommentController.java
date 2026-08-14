package ru.practicum.ewm.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.ewm.dto.CommentDto;
import ru.practicum.ewm.dto.NewCommentDto;
import ru.practicum.ewm.dto.UpdateCommentDto;

import java.util.List;

import static ru.practicum.ewm.controller.ControllerConstants.DEFAULT_FROM;
import static ru.practicum.ewm.controller.ControllerConstants.DEFAULT_SIZE;
import static ru.practicum.ewm.controller.ControllerConstants.MIN_FROM;

/**
 * Private API for managing a user's comments.
 */
@Validated
@RequestMapping("/users/{userId}/comments")
public interface PrivateCommentController {
    /** Adds a comment to a published event. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CommentDto createComment(@PathVariable Long userId,
                             @RequestParam Long eventId,
                             @Valid @RequestBody NewCommentDto request);

    /** Updates a comment owned by the user. */
    @PatchMapping("/{commentId}")
    CommentDto updateComment(@PathVariable Long userId,
                             @PathVariable Long commentId,
                             @Valid @RequestBody UpdateCommentDto request);

    /** Deletes a comment owned by the user. */
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteComment(@PathVariable Long userId, @PathVariable Long commentId);

    /** Returns comments written by the user. */
    @GetMapping
    List<CommentDto> getUserComments(@PathVariable Long userId,
                                     @RequestParam(defaultValue = DEFAULT_FROM) @Min(MIN_FROM) int from,
                                     @RequestParam(defaultValue = DEFAULT_SIZE) @Positive int size);
}
