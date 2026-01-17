package ru.practicum.event.service.services.interfaces;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import ru.practicum.common.exception.BadArgumentsException;
import ru.practicum.common.exception.ConflictException;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.event.api.dto.EventAdminUpdateDto;
import ru.practicum.event.api.dto.EventFullDto;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {

    EventFullDto updateEvent(EventAdminUpdateDto eventAdminUpdateDto, long eventId)
            throws NotFoundException, BadRequestException, ConflictException;


    Page<EventFullDto> getEventsForAdmin(List<Long> users,
                                         List<String> states,
                                         List<Long> categories,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd,
                                         Integer from,
                                         Integer size) throws BadArgumentsException;
}
