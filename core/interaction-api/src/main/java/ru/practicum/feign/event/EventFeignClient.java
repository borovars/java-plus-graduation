package ru.practicum.feign.event;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "event-service", path = "/internal/event")
public interface EventFeignClient extends InternalEventContract {
}
