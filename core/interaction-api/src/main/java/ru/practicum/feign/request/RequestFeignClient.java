package ru.practicum.feign.request;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "request-service", path = "/internal/request")
public interface RequestFeignClient extends InternalRequestContract {
}
