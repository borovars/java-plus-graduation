package ru.practicum.feign.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.feign.user.dto.UserDto;

public interface InternalUserContract {

    @GetMapping("/{userId}")
    UserDto findByUserId(@PathVariable @NotNull @Positive Long userId) throws NotFoundException;
}
