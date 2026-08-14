package ru.practicum.ewm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static ru.practicum.ewm.model.CommentConstraints.MAX_TEXT_LENGTH;

public record NewCommentDto(
        @NotBlank
        @Size(max = MAX_TEXT_LENGTH)
        String text) {
}
