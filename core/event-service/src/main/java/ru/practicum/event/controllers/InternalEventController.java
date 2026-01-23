package ru.practicum.event.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.event.EventMapper;
import ru.practicum.feign.event.InternalEventContract;
import ru.practicum.event.services.InternalEventService;
import ru.practicum.feign.event.dto.EventFullDto;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/event")
public class InternalEventController implements InternalEventContract {

    private final InternalEventService eventService;

    @Override
    public boolean existsByCategoryId(Long categoryId) {
        return eventService.existsByCategoryId(categoryId);
    }

    @Override
    public Set<Long> findAllById(Set<Long> eventsIds) {
        return eventService.findAllById(eventsIds);
    }

    @Override
    public boolean existsById(Long eventId) {
        return eventService.existsById(eventId);
    }

    @Override
    public EventFullDto findEventById(Long eventId) throws NotFoundException {
        return eventService.findEventById(eventId);
    }

    @Override
    public Set<EventFullDto> findAllByIdFull(List<Long> eventsIds){
        return eventService.findAllByIdFull(eventsIds);
    }
}
