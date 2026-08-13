package ru.practicum.ewm.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;

/**
 * Administrative category API.
 */
@Validated
@RequestMapping("/admin/categories")
public interface AdminCategoryController {
    /** Creates a category. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CategoryDto createCategory(@Valid @RequestBody NewCategoryDto request);

    /** Updates a category. */
    @PatchMapping("/{categoryId}")
    CategoryDto updateCategory(@PathVariable("categoryId") Long categoryId,
                               @Valid @RequestBody CategoryDto request);

    /** Deletes an empty category. */
    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCategory(@PathVariable("categoryId") Long categoryId);
}
