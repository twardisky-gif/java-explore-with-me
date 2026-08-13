package ru.practicum.ewm.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ewm.dto.CategoryDto;

import java.util.List;

import static ru.practicum.ewm.controller.ControllerConstants.DEFAULT_FROM;
import static ru.practicum.ewm.controller.ControllerConstants.DEFAULT_SIZE;
import static ru.practicum.ewm.controller.ControllerConstants.MIN_FROM;

/**
 * Public category API.
 */
@Validated
@RequestMapping("/categories")
public interface PublicCategoryController {
    /** Returns a category by identifier. */
    @GetMapping("/{categoryId}")
    CategoryDto getCategory(@PathVariable Long categoryId);

    /** Returns categories with offset pagination. */
    @GetMapping
    List<CategoryDto> getCategories(@RequestParam(defaultValue = DEFAULT_FROM) @Min(MIN_FROM) int from,
                                    @RequestParam(defaultValue = DEFAULT_SIZE) @Positive int size);
}
