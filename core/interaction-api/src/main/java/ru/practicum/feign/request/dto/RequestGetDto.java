package ru.practicum.feign.request.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.feign.request.enums.RequestStatus;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestGetDto {
    Long id;

    LocalDateTime created;

    Long event;

    Long requester;

    RequestStatus status;
}
