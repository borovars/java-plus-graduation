package ru.practicum.analyzer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "user_actions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long eventId;

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    private Double actionWeight;

    private Instant timestamp;
}