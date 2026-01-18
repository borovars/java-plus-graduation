package ru.practicum.feign.event.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.practicum.feign.event.enums.StateActionsAdmin;
import ru.practicum.feign.location.dto.LocationDto;

import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventAdminUpdateDto {


    @Size(min = 20, max = 2000)
    private String annotation;

    @Positive
    private Long category;

    @Size(min = 20, max = 7000)
    private String description;

    @Future
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    @PositiveOrZero(message = "Лимит участников не может быть отрицательным или равен нулю")
    private Integer participantLimit;

    private Boolean requestModeration;

    StateActionsAdmin stateAction;

    @Size(min = 3, max = 120)
    String title;
}
