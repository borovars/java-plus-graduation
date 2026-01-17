package ru.practicum.user.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.api.dto.UserDto;

@FeignClient(name = "user-service", path = "/admin/users")
public interface UserFeignClient {

    @GetMapping("/{userId}")
    UserDto findByUserId(@PathVariable Long userId);
}
