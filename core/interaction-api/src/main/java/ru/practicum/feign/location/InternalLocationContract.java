package ru.practicum.feign.location;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.feign.location.dto.LocationDto;

public interface InternalLocationContract {

    @PostMapping
    Long save(@RequestBody LocationDto locationDto);
}
