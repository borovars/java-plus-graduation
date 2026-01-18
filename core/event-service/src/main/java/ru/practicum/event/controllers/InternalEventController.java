package ru.practicum.event.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.feign.event.InternalEventContract;
import ru.practicum.event.services.InternalEventService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/event")
public class InternalEventController implements InternalEventContract {

    private final InternalEventService eventService;

    @Override
    public boolean existsByCategoryId(Long categoryId){
        return eventService.existsByCategoryId(categoryId);
    }
}
