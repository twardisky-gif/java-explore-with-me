package ru.practicum.ewm.controller;

import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.dto.UpdateCompilationDto;
import ru.practicum.ewm.service.CompilationService;

@RestController
public class AdminCompilationControllerImpl implements AdminCompilationController {
    private final CompilationService compilationService;

    public AdminCompilationControllerImpl(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto request) {
        return compilationService.create(request);
    }

    @Override
    public CompilationDto updateCompilation(Long compilationId, UpdateCompilationDto request) {
        return compilationService.update(compilationId, request);
    }

    @Override
    public void deleteCompilation(Long compilationId) {
        compilationService.delete(compilationId);
    }

}
