package ru.practicum.feign.location;

import org.springframework.web.bind.annotation.PostMapping;
import ru.practicum.feign.location.dto.LocationDto;

public interface InternalLocationContract {

    @PostMapping
    Long save(LocationDto locationDto);
}
