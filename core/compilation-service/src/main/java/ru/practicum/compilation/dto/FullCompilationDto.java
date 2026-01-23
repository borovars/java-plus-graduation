package ru.practicum.compilation.dto;

import lombok.*;
import ru.practicum.feign.event.dto.EventFullDto;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FullCompilationDto {

    /**
     * Идентификатор
     */
    private Long id;

    /**
     * События входящие в подборку
     */
    private Set<EventFullDto> events;

    /**
     * Закреплена ли подборка на главной странице сайта
     */
    private Boolean pinned;

    /**
     * Заголовок подборки
     */
    private String title;
}