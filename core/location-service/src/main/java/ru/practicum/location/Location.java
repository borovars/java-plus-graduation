package ru.practicum.location;

import jakarta.persistence.*;
import lombok.*;

/**
 * Место проведения события.
 */
@Table(name = "locations")
@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Широта
     */
    @Column(name = "lat")
    private Float lat;

    /**
     * Долгота
     */
    @Column(name = "lon")
    private Float lon;
}
