package ru.practicum.feign.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.common.exception.ConflictException;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.feign.request.dto.RequestGetDto;
import ru.practicum.feign.request.dto.RequestsChangeStatusRequestDto;
import ru.practicum.feign.request.dto.RequestsChangeStatusResponseDto;

import java.util.List;

public interface InternalRequestContract {

    @GetMapping("/{userId}/events/{eventId}")
    List<RequestGetDto> getRequestsByEventId(@PathVariable(name = "userId") @Positive Long userId,
                                             @PathVariable(name = "eventId") @Positive Long eventId)
            throws ConflictException, NotFoundException;

    @PatchMapping("/{userId}/events/{eventId}")
    RequestsChangeStatusResponseDto changeRequestsStatus(@PathVariable(name = "userId") @Positive Long userId,
                                                         @PathVariable(name = "eventId") @Positive Long eventId,
                                                         @RequestBody @Valid RequestsChangeStatusRequestDto dto) throws ConflictException, NotFoundException;
}
