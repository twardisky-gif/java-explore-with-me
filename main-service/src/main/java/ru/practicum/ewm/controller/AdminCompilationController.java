package ru.practicum.ewm.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.dto.UpdateCompilationDto;

/**
 * Administrative compilation API.
 */
@Validated
@RequestMapping("/admin/compilations")
public interface AdminCompilationController {
    /** Creates a compilation. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto request);

    /** Updates a compilation. */
    @PatchMapping("/{compilationId}")
    CompilationDto updateCompilation(@PathVariable("compilationId") Long compilationId,
                                     @Valid @RequestBody UpdateCompilationDto request);

    /** Deletes a compilation. */
    @DeleteMapping("/{compilationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCompilation(@PathVariable("compilationId") Long compilationId);
}
