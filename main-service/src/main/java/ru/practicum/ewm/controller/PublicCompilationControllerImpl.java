package ru.practicum.ewm.controller;

import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.service.CompilationService;

import java.util.List;

@RestController
public class PublicCompilationControllerImpl implements PublicCompilationController {
    private final CompilationService compilationService;

    public PublicCompilationControllerImpl(CompilationService compilationService) {
        this.compilationService = compilationService;
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
