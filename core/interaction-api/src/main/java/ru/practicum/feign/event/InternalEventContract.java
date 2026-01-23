package ru.practicum.feign.event;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.feign.event.dto.EventFullDto;

import java.util.List;
import java.util.Set;

public interface InternalEventContract {

    @GetMapping
    boolean existsByCategoryId(@RequestParam(name = "categoryId") @NotNull @Positive Long categoryId);

    @GetMapping("/all")
    Set<Long> findAllById(@RequestParam Set<Long> eventsIds);

    @GetMapping("/exists/{eventId}")
    boolean existsById(@PathVariable(name = "eventId") @NotNull @Positive Long eventId);

    @GetMapping("/{eventId}")
    EventFullDto findEventById(@PathVariable(name = "eventId") @NotNull @Positive  Long eventId) throws NotFoundException;

    @GetMapping("/all/full")
    Set<EventFullDto> findAllByIdFull(@RequestParam(name = "eventsIds") List<Long> eventsIds);
}
