package ru.practicum.request;

import lombok.experimental.UtilityClass;
import ru.practicum.feign.request.dto.RequestGetDto;

@UtilityClass
public final class RequestMapper {

    public RequestGetDto toRequestGetDto(Request request) {
        return RequestGetDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .requester(request.getRequester())
                .event(request.getEvent())
                .status(request.getStatus())
                .build();
    }
}
