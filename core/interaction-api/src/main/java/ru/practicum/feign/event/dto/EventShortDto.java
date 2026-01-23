package ru.practicum.feign.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {

    private Long id;
    private String annotation;
    private Long category;
    private String eventDate;
    private Long initiator;
    private Boolean paid;
    private String title;
    private Long views;
    private Integer confirmedRequests;
}
