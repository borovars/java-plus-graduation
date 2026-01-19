package ru.practicum.compilation.dto;

import lombok.*;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {

    /**
     * Идентификатор
     */
    private Long id;

    /**
     * События входящие в подборку
     */
    private Set<Long> events;

    /**
     * Закреплена ли подборка на главной странице сайта
     */
    private Boolean pinned;

    /**
     * Заголовок подборки
     */
    private String title;
}