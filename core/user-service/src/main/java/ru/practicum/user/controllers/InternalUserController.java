package ru.practicum.user.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.feign.user.InternalUserContract;
import ru.practicum.feign.user.dto.UserDto;
import ru.practicum.user.services.UserService;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class InternalUserController implements InternalUserContract {

    private final UserService userService;

    @Override
    public UserDto findByUserId(Long userId) throws NotFoundException {
        return userService.findById(userId);
    }
}
