package ru.practicum.event.service.controllers;

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
import ru.practicum.event.api.EventFeignClient;
import ru.practicum.event.api.dto.EventAdminUpdateDto;
import ru.practicum.event.api.dto.EventFullDto;
import ru.practicum.event.service.services.interfaces.AdminEventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminEventController implements EventFeignClient {

    private final AdminEventService adminEventService;

    @Override
    public EventFullDto updateEventByAdmin(EventAdminUpdateDto eventAdminUpdateDto, long eventId)
            throws NotFoundException, BadRequestException, ConflictException {
        return adminEventService.updateEvent(eventAdminUpdateDto, eventId);
    }

    @Override
    public List<EventFullDto> getEvents(List<Long> users,
                                        List<String> states,
                                        List<Long> categories,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        Integer from,
                                        Integer size,
                                        HttpServletResponse response) throws BadArgumentsException {

        Page<EventFullDto> page = adminEventService.getEventsForAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
        response.setHeader("X-Total-Count", String.valueOf(page.getTotalElements()));

        return page.getContent();
    }
}
