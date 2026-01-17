package ru.practicum.user.api;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.exception.ConflictException;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.user.api.dto.UserCreateDto;
import ru.practicum.user.api.dto.UserDto;

import java.util.Collection;
import java.util.List;

/**
 * API для работы с пользователями
 */
@FeignClient(name = "user-service", path = "/admin/users")
public interface UserFeignClient {

    /**
     * Получение информации о пользователях
     *
     * @param ids  id пользователей
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size количество элементов в наборе
     * @return коллекция {@link UserDto}
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Collection<UserDto> getUsers(@RequestParam(name = "ids", required = false) List<Long> ids,
                                 @RequestParam(name = "from", required = false, defaultValue = "0") int from,
                                 @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                 HttpServletResponse response);

    /**
     * Добавление нового пользователя
     *
     * @param dto Данные добавляемого пользователя
     * @return Данные добавленного пользователя
     * @throws ConflictException если переданный адрес почты уже используется
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserDto createUser(@RequestBody @Valid UserCreateDto dto) throws ConflictException;

    /**
     * Удаление пользователя
     *
     * @param userId id пользователя
     * @throws NotFoundException если пользователь не найден по переданному идентификатору
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable long userId) throws NotFoundException;
}
