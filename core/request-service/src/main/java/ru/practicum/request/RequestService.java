package ru.practicum.request;

import org.springframework.data.domain.Page;
import ru.practicum.common.exception.ConflictException;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.feign.request.dto.RequestGetDto;
import ru.practicum.feign.request.dto.RequestsChangeStatusRequestDto;
import ru.practicum.feign.request.dto.RequestsChangeStatusResponseDto;

import java.util.List;

public interface RequestService {

    Page<RequestGetDto> getRequestsByUserId(long userId, int from, int size)
            throws NotFoundException;

    RequestGetDto createRequest(long userId, long eventId)
            throws NotFoundException, ConflictException;

    RequestGetDto cancelRequest(long userId, long requestId)
            throws NotFoundException, ConflictException;

    List<RequestGetDto> getRequestsByEventId(Long userId, Long eventId)
            throws ConflictException, NotFoundException;

    RequestsChangeStatusResponseDto requestsChangeStatus(Long userId, Long eventId, RequestsChangeStatusRequestDto dto)
            throws ConflictException, NotFoundException;

    int getConfirmedRequest(Long eventId);

    Boolean checkRegistration(Long eventId, Long userId);
}
