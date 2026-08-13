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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;

import java.util.List;

/**
 * Public and administrative category operations.
 */
@Validated
public interface CategoryController {
    /** Creates a category. */
    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    CategoryDto createCategory(@Valid @RequestBody NewCategoryDto request);

    /** Updates a category. */
    @PatchMapping("/admin/categories/{categoryId}")
    CategoryDto updateCategory(@PathVariable("categoryId") Long categoryId,
                               @Valid @RequestBody CategoryDto request);

    /** Deletes an empty category. */
    @DeleteMapping("/admin/categories/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCategory(@PathVariable("categoryId") Long categoryId);

    /** Returns a category. */
    @GetMapping("/categories/{categoryId}")
    CategoryDto getCategory(@PathVariable("categoryId") Long categoryId);

    /** Returns categories with offset pagination. */
    @GetMapping("/categories")
    List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") @Min(0) int from,
                                    @RequestParam(defaultValue = "10") @Positive int size);
}
