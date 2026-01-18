package ru.practicum.location;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.feign.location.InternalLocationContract;
import ru.practicum.feign.location.dto.LocationDto;

@RestController
@RequestMapping("/internal/location")
@RequiredArgsConstructor
public class InternalLocationController implements InternalLocationContract {

    private final LocationService locationService;

    @Override
    public Long save(LocationDto locationDto){
        return locationService.save(locationDto);
    }
}
