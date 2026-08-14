package ru.practicum.ewm.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ewm.dto.CommentDto;

import java.util.List;

import static ru.practicum.ewm.controller.ControllerConstants.DEFAULT_FROM;
import static ru.practicum.ewm.controller.ControllerConstants.DEFAULT_SIZE;
import static ru.practicum.ewm.controller.ControllerConstants.MIN_FROM;

/**
 * Public API for reading comments.
 */
@Validated
@RequestMapping("/comments")
public interface PublicCommentController {
    /** Returns a comment by identifier. */
    @GetMapping("/{commentId}")
    CommentDto getComment(@PathVariable Long commentId);

    /** Returns comments for a published event. */
    @GetMapping
    List<CommentDto> getEventComments(@RequestParam Long eventId,
                                      @RequestParam(defaultValue = DEFAULT_FROM) @Min(MIN_FROM) int from,
                                      @RequestParam(defaultValue = DEFAULT_SIZE) @Positive int size);
}
