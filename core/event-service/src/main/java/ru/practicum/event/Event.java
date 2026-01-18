package ru.practicum.event;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;
import ru.practicum.category.Category;
import ru.practicum.feign.event.enums.States;
import ru.practicum.location.Location;
import ru.practicum.user.User;

import java.time.LocalDateTime;

/**
 * Событие.
 */
@Table(name = "events")
@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Краткое описание
     */
    @Column(name = "annotation")
    private String annotation;

    /**
     * Категория
     */
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Long category;

    /**
     * Полное описание
     */
    @Column(name = "description")
    private String description;

    /**
     * Дата проведения
     */
    @Column(name = "event_date")
    private LocalDateTime eventDate;

    /**
     * Место проведения
     */
    @OneToOne
    @JoinColumn(name = "location_id")
    private Long location;

    /**
     * Признак оплаты
     */
    @Column(name = "paid")
    private Boolean paid;

    /**
     * Максимальное количество участников
     */
    @Column(name = "participant_limit")
    private Integer participantLimit;

    /**
     * Признак модерации заявок
     */
    @Column(name = "request_moderation")
    private Boolean requestModeration;

    /**
     * Заголовок
     */
    @Column(name = "title")
    private String title;

    /**
     * Дата создания
     */
    @Column(name = "created_on")
    private LocalDateTime createdOn;

    /**
     * Дата публикации
     */
    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    /**
     * Инициатор
     */
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private Long initiator;

    /**
     * Состояние
     */
    @Enumerated(EnumType.STRING)
    private States state;

    @Formula("(select count(*) from requests p " +
            " where p.event_id = id and p.status = 'CONFIRMED')")
    private Long confirmedRequests;
}
