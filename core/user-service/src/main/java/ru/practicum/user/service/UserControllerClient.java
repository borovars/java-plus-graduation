package ru.practicum.user.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.exception.ConflictException;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.user.api.UserFeignClient;
import ru.practicum.user.api.dto.UserCreateDto;
import ru.practicum.user.api.dto.UserDto;

import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserControllerClient implements UserFeignClient {

    private final UserService userService;

    @Override
    public Collection<UserDto> getUsers(List<Long> ids, int from, int size, HttpServletResponse response) {

        Page<UserDto> page = userService.getUsers(ids, from, size);
        response.setHeader("X-Total-Count", String.valueOf(page.getTotalElements()));

        return page.getContent();
    }

    @Override
    public UserDto createUser(UserCreateDto dto) throws ConflictException {

        return userService.createUser(dto);
    }

    @Override
    public void deleteUser(long userId) throws NotFoundException {
        userService.deleteUser(userId);
    }
}
