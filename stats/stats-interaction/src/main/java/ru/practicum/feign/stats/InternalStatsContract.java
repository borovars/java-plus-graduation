package ru.practicum.feign.stats;

import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface InternalStatsContract {

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    HitDto postHit(@RequestBody @Valid HitDto hitDto);

    @GetMapping
    List<StatsDto> getStats(
            @RequestParam("start")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime start,

            @RequestParam("end")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime end,

            @RequestParam(value = "uris", required = false)
            List<String> uris,

            @RequestParam(value = "unique", defaultValue = "false")
            Boolean unique
    ) throws BadRequestException;
}
