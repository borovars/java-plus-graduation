package ru.practicum.feign.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.feign.event.enums.States;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventFullDto {

    private Long id;
    private String annotation;
    private Long category;
    private Long confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private Long initiator;
    private Long location;
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private States state;
    private String title;
    private Long views;
}
