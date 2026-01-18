package ru.practicum.feign.user;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user-service", path = "/internal/users")
public interface UserFeignClient extends InternalUserContract {
}
