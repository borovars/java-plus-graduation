package ru.practicum.request;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.feign.request.enums.RequestStatus;

import java.time.LocalDateTime;

/**
 * Событие.
 */
@Table(name = "requests")
@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Request {

    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Дата время создания
     */
    @Column(name = "created")
    private LocalDateTime created;

    /**
     * Событие
     */
    @Column(name = "event_id")
    private Long event;

    /**
     * Пользователь, создавший запрос
     */
    @Column(name = "requester_id")
    private Long requester;

    /**
     * Статус
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status;
}
