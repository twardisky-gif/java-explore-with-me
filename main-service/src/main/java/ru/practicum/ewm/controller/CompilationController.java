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
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.dto.UpdateCompilationRequest;

import java.util.List;

/**
 * Public and administrative compilation operations.
 */
@Validated
public interface CompilationController {
    /** Creates a compilation. */
    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto request);

    /** Updates a compilation. */
    @PatchMapping("/admin/compilations/{compilationId}")
    CompilationDto updateCompilation(@PathVariable("compilationId") Long compilationId,
                                     @Valid @RequestBody UpdateCompilationRequest request);

    /** Deletes a compilation. */
    @DeleteMapping("/admin/compilations/{compilationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCompilation(@PathVariable("compilationId") Long compilationId);

    /** Returns a compilation. */
    @GetMapping("/compilations/{compilationId}")
    CompilationDto getCompilation(@PathVariable("compilationId") Long compilationId);

    /** Returns compilations selected by pin state and pagination. */
    @GetMapping("/compilations")
    List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                         @RequestParam(defaultValue = "0") @Min(0) int from,
                                         @RequestParam(defaultValue = "10") @Positive int size);
}
