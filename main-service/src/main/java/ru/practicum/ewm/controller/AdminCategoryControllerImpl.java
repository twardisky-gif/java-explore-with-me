package ru.practicum.ewm.controller;

import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;
import ru.practicum.ewm.service.CategoryService;

@RestController
public class AdminCategoryControllerImpl implements AdminCategoryController {
    private final CategoryService categoryService;

    public AdminCategoryControllerImpl(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public CategoryDto createCategory(NewCategoryDto request) {
        return categoryService.create(request);
    }

    @Override
    public CategoryDto updateCategory(Long categoryId, CategoryDto request) {
        return categoryService.update(categoryId, request);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryService.delete(categoryId);
    }

}
