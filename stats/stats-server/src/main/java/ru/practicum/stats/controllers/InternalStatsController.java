package ru.practicum.stats.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.feign.stats.InternalStatsContract;
import ru.practicum.stats.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/stats")
public class InternalStatsController implements InternalStatsContract {

    private final StatsService statsService;

    @Override
    public HitDto postHit(HitDto hitRequestDto) {
        return statsService.createHit(hitRequestDto);
    }

    @Override
    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique)
            throws BadRequestException {
        return statsService.getStats(start, end, uris, unique);
    }
}