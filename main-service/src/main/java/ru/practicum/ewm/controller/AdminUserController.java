package ru.practicum.ewm.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.ewm.dto.NewUserDto;
import ru.practicum.ewm.dto.UserDto;

import java.util.List;

import static ru.practicum.ewm.controller.ControllerConstants.DEFAULT_FROM;
import static ru.practicum.ewm.controller.ControllerConstants.DEFAULT_SIZE;
import static ru.practicum.ewm.controller.ControllerConstants.MIN_FROM;

/**
 * Administrative operations for users.
 */
@Validated
@RequestMapping("/admin/users")
public interface AdminUserController {
    /** Returns users selected by identifiers and pagination. */
    @GetMapping
    List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                           @RequestParam(defaultValue = DEFAULT_FROM) @Min(MIN_FROM) int from,
                           @RequestParam(defaultValue = DEFAULT_SIZE) @Positive int size);

    /** Registers a user. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserDto createUser(@Valid @RequestBody NewUserDto request);

    /** Deletes a user. */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable Long userId);
}
