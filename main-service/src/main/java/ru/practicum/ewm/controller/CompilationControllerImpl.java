package ru.practicum.ewm.controller;

import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.dto.UpdateCompilationRequest;
import ru.practicum.ewm.service.CompilationService;

import java.util.List;

@RestController
public class CompilationControllerImpl implements CompilationController {
    private final CompilationService compilationService;

    public CompilationControllerImpl(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto request) {
        return compilationService.create(request);
    }

    @Override
    public CompilationDto updateCompilation(Long compilationId, UpdateCompilationRequest request) {
        return compilationService.update(compilationId, request);
    }

    @Override
    public void deleteCompilation(Long compilationId) {
        compilationService.delete(compilationId);
    }

    @Override
    public CompilationDto getCompilation(Long compilationId) {
        return compilationService.get(compilationId);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        return compilationService.getAll(pinned, from, size);
    }
}
