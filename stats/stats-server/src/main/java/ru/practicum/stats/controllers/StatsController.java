package ru.practicum.stats.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.stats.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitDto createHit(@Valid @RequestBody HitDto hitRequestDto) {
        return statsService.createHit(hitRequestDto);
    }

    @GetMapping("/stats")
    public List<StatsDto> getStats(@RequestParam(name = "start", required = true)
                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                   LocalDateTime start,
                                   @RequestParam(name = "end", required = true)
                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                   LocalDateTime end,
                                   @RequestParam(name = "uris", required = false) List<String> uris,
                                   @RequestParam(name = "unique", defaultValue = "false") Boolean unique) throws BadRequestException {
        return statsService.getStats(start, end, uris, unique);
    }
}
