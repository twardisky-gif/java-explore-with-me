package ru.practicum.ewm.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;
import ru.practicum.ewm.entity.Category;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.EntityMapper;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.OffsetPageRequest;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public CategoryService(CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional
    public CategoryDto create(NewCategoryDto request) {
        Category category = new Category();
        category.setName(request.name());
        return EntityMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Transactional
    public CategoryDto update(Long categoryId, CategoryDto request) {
        Category category = getEntity(categoryId);
        category.setName(request.name());
        return EntityMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Transactional
    public void delete(Long categoryId) {
        Category category = getEntity(categoryId);
        if (eventRepository.existsByCategoryId(categoryId)) {
            throw new ConflictException("The category is not empty");
        }
        categoryRepository.delete(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getAll(int from, int size) {
        return categoryRepository.findAll(new OffsetPageRequest(from, size, Sort.by("id"))).stream()
                .map(EntityMapper::toCategoryDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryDto get(Long categoryId) {
        return EntityMapper.toCategoryDto(getEntity(categoryId));
    }

    @Transactional(readOnly = true)
    public Category getEntity(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + categoryId + " was not found"));
    }
}
