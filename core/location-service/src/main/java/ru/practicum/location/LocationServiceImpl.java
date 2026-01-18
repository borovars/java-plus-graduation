package ru.practicum.location;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.feign.location.dto.LocationDto;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Override
    public Long save(LocationDto locationDto) {
        return locationRepository.save(LocationMapper.mapToLocation(locationDto)).getId();
    }
}
