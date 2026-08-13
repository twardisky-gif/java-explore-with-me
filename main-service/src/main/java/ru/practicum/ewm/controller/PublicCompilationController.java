package ru.practicum.ewm.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ewm.dto.CompilationDto;

import java.util.List;

import static ru.practicum.ewm.controller.ControllerConstants.DEFAULT_FROM;
import static ru.practicum.ewm.controller.ControllerConstants.DEFAULT_SIZE;
import static ru.practicum.ewm.controller.ControllerConstants.MIN_FROM;

/**
 * Public compilation API.
 */
@Validated
@RequestMapping("/compilations")
public interface PublicCompilationController {
    /** Returns a compilation by identifier. */
    @GetMapping("/{compilationId}")
    CompilationDto getCompilation(@PathVariable Long compilationId);

    /** Returns compilations selected by pin state and pagination. */
    @GetMapping
    List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                         @RequestParam(defaultValue = DEFAULT_FROM) @Min(MIN_FROM) int from,
                                         @RequestParam(defaultValue = DEFAULT_SIZE) @Positive int size);
}
