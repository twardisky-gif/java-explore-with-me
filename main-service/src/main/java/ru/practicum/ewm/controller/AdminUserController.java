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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.ewm.dto.NewUserRequest;
import ru.practicum.ewm.dto.UserDto;

import java.util.List;

/**
 * Administrative operations for users.
 */
@Validated
public interface AdminUserController {
    /** Returns users selected by identifiers and pagination. */
    @GetMapping("/admin/users")
    List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                           @RequestParam(defaultValue = "0") @Min(0) int from,
                           @RequestParam(defaultValue = "10") @Positive int size);

    /** Registers a user. */
    @PostMapping("/admin/users")
    @ResponseStatus(HttpStatus.CREATED)
    UserDto createUser(@Valid @RequestBody NewUserRequest request);

    /** Deletes a user. */
    @DeleteMapping("/admin/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable Long userId);
}
