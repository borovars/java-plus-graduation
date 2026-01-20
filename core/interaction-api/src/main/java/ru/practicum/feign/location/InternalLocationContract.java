package ru.practicum.feign.location;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.feign.location.dto.LocationDto;

public interface InternalLocationContract {

    @PostMapping
    Long save(@RequestBody LocationDto locationDto);

    @GetMapping("/{locationId}")
    LocationDto get(@PathVariable(name = "locationId") Long locationId);
}
