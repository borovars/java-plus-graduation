package ru.practicum.analyzer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "similarities")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Similarity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long eventA;
    private Long eventB;
    private Double score;
    private Instant timestamp;
}