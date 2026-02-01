package ru.practicum.feign.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.feign.event.enums.States;
import ru.practicum.feign.location.dto.LocationDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventFullDto {

    private Long id;
    private String annotation;
    private Long category;
    private String createdOn;
    private String description;
    private String eventDate;
    private Long initiator;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private States state;
    private String title;
    private Double rating;
    private Integer confirmedRequests;
}
