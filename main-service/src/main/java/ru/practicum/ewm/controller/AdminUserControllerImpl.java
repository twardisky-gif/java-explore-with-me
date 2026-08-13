package ru.practicum.ewm.controller;

import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.NewUserRequest;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.service.UserService;

import java.util.List;

@RestController
public class AdminUserControllerImpl implements AdminUserController {
    private final UserService userService;

    public AdminUserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        return userService.getAll(ids, from, size);
    }

    @Override
    public UserDto createUser(NewUserRequest request) {
        return userService.create(request);
    }

    @Override
    public void deleteUser(Long userId) {
        userService.delete(userId);
    }
}
