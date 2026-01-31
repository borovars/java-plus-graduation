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
import java.util.Map;

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

    @Override
    public int findConfirmedRequests(Long eventId) {
        return requestService.getConfirmedRequest(eventId);
    }

    @Override
    public Boolean checkRegistration(Long eventId, Long userId) {
        return requestService.checkRegistration(eventId, userId);
    }

    @Override
    public Map<Long, Integer> findListOfConfirmedRequests(List<Long> eventsIds) {
        return requestService.findListOfConfirmedRequests(eventsIds);
    }
}
