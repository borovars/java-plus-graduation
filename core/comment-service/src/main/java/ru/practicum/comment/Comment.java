package ru.practicum.comment;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Комментарий.
 */
@Entity
@Table(name = "comments")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Comment {

    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Автор
     */
    @Column(name = "author_id")
    private Long author;

    /**
     * Комментируемое событие
     */
    @Column(name = "event_id")
    private Long event;

    /**
     * Содержимое
     */
    @Column(name = "text")
    private String text;

    /**
     * Дата создания
     */
    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Override
    public final boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comment comment)) {
            return false;
        }

        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
