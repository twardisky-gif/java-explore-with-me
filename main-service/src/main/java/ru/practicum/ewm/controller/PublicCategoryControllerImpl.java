package ru.practicum.ewm.controller;

import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.service.CategoryService;

import java.util.List;

@RestController
public class PublicCategoryControllerImpl implements PublicCategoryController {
    private final CategoryService categoryService;

    public PublicCategoryControllerImpl(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public CategoryDto getCategory(Long categoryId) {
        return categoryService.get(categoryId);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        return categoryService.getAll(from, size);
    }
}
