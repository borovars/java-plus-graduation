package ru.practicum.feign.location;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "LOCATION-SERVICE", path = "/internal/location")
public interface LocationFeignClient extends InternalLocationContract {
}
