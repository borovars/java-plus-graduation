package ru.practicum.event.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.exception.BadArgumentsException;
import ru.practicum.common.exception.ConflictException;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.feign.event.dto.EventAdminUpdateDto;
import ru.practicum.feign.event.dto.EventFullDto;
import ru.practicum.event.services.interfaces.AdminEventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class AdminEventController {

    private final AdminEventService adminEventService;

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(@RequestBody @Valid EventAdminUpdateDto eventAdminUpdateDto,
                                           @PathVariable(name = "eventId") @Positive long eventId)
            throws NotFoundException, BadRequestException, ConflictException {
        return adminEventService.updateEvent(eventAdminUpdateDto, eventId);
    }

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(required = false) List<Long> users,
                                        @RequestParam(required = false) List<String> states,
                                        @RequestParam(required = false) List<Long> categories,

                                        @RequestParam(required = false)
                                        LocalDateTime rangeStart,
                                        @RequestParam(required = false)
                                        LocalDateTime rangeEnd,

                                        @RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(defaultValue = "10") Integer size,
                                        HttpServletResponse response) throws BadArgumentsException {

        Page<EventFullDto> page = adminEventService.getEventsForAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
        response.setHeader("X-Total-Count", String.valueOf(page.getTotalElements()));

        return page.getContent();
    }
}
