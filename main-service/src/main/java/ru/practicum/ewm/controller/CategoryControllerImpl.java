package ru.practicum.ewm.controller;

import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;
import ru.practicum.ewm.service.CategoryService;

import java.util.List;

@RestController
public class CategoryControllerImpl implements CategoryController {
    private final CategoryService categoryService;

    public CategoryControllerImpl(CategoryService categoryService) {
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

    @Override
    public CategoryDto getCategory(Long categoryId) {
        return categoryService.get(categoryId);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        return categoryService.getAll(from, size);
    }
}
