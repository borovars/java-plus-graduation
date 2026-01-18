package ru.practicum.location;

import ru.practicum.feign.location.dto.LocationDto;

public interface LocationService {
    Long save(LocationDto locationDto);
}
