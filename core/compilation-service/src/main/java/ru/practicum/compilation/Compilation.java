package ru.practicum.compilation;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Подборка событий.
 */
@Table(name = "compilations")
@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Compilation {

    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Заголовок подборки
     */
    @Column(name = "title", nullable = false, unique = true)
    private String title;

    /**
     * Закреплена ли подборка на главной странице сайта
     */
    @Column(name = "pinned")
    private Boolean pinned;

    @Transient
    private Set<Long> events = new HashSet<>();
}