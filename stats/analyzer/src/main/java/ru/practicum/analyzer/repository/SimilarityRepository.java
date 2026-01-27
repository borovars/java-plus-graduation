package ru.practicum.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.analyzer.model.Similarity;

import java.util.List;

public interface SimilarityRepository extends JpaRepository<Similarity, Long> {

    List<Similarity> findByEventA(Long eventId);
    List<Similarity> findByEventB(Long eventId);
    List<Similarity> findByEventAIn(List<Long> eventIds);
    List<Similarity> findByEventBIn(List<Long> eventIds);
}