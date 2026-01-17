package ru.practicum.event;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "event-service", path = "")
public class EventFeignClient {

    @GetMapping("/{categoryId}")

}
