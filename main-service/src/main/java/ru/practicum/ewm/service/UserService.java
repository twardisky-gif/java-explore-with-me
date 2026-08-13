package ru.practicum.ewm.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.NewUserRequest;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.entity.User;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.EntityMapper;
import ru.practicum.ewm.repository.OffsetPageRequest;
import ru.practicum.ewm.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserDto create(NewUserRequest request) {
        User user = new User();
        user.setEmail(request.email());
        user.setName(request.name());
        return EntityMapper.toUserDto(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAll(List<Long> ids, int from, int size) {
        OffsetPageRequest page = new OffsetPageRequest(from, size, Sort.by("id"));
        if (ids == null || ids.isEmpty()) {
            return userRepository.findAll(page).stream().map(EntityMapper::toUserDto).toList();
        }
        return userRepository.findByIdIn(ids, page).stream().map(EntityMapper::toUserDto).toList();
    }

    @Transactional
    public void delete(Long userId) {
        User user = getEntity(userId);
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public User getEntity(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
    }
}
