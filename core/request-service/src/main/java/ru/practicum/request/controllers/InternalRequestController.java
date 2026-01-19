package ru.practicum.request.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.common.exception.ConflictException;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.feign.request.InternalRequestContract;
import ru.practicum.feign.request.dto.RequestGetDto;
import ru.practicum.feign.request.dto.RequestsChangeStatusRequestDto;
import ru.practicum.feign.request.dto.RequestsChangeStatusResponseDto;
import ru.practicum.request.RequestService;

import java.util.List;

@RestController
@RequestMapping("/internal/request")
@RequiredArgsConstructor
public class InternalRequestController implements InternalRequestContract {

    private final RequestService requestService;

    @Override
    public List<RequestGetDto> getRequestsByEventId(Long userId, Long eventId)
            throws ConflictException, NotFoundException {
        return requestService.getRequestsByEventId(userId, eventId);
    }

    @Override
    public RequestsChangeStatusResponseDto changeRequestsStatus(Long userId, Long eventId,
                                                                RequestsChangeStatusRequestDto dto)
            throws ConflictException, NotFoundException {
        return requestService.requestsChangeStatus(userId, eventId, dto);
    }
}
