package ru.practicum.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.feign.event.enums.States;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findAllByInitiator(Long id, Pageable pageable);

    @Query("""
                SELECT e
                FROM Event AS e
                WHERE (?1 IS NULL OR e.initiator IN ?1)
                AND (?2 IS NULL OR e.state IN ?2)
                AND (?3 IS NULL OR e.category IN ?3)
                AND (CAST(?4 AS timestamp) IS NULL OR e.eventDate >= ?4)
                AND (CAST(?5 AS timestamp) IS NULL OR e.eventDate < ?5)
            """)
    Page<Event> findAllByFiltersAdmin(List<Long> users, List<String> states, List<Long> categories,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("""
                SELECT e
                FROM Event AS e
                WHERE e.state = PUBLISHED
                AND (?1 IS NULL OR e.annotation ILIKE %?1% OR e.description ILIKE %?1%)
                AND (?2 IS NULL OR e.category IN ?2)
                AND (?3 IS NULL OR e.paid = ?3)
                AND (CAST(?4 AS timestamp) IS NULL AND e.eventDate >= CURRENT_TIMESTAMP OR e.eventDate >= ?4)
                AND (CAST(?5 AS timestamp) IS NULL OR e.eventDate < ?5)
            """)
    Page<Event> findAllByFiltersPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                       LocalDateTime rangeEnd, Boolean onlyAvailable, Pageable pageable);

    boolean existsByCategory(Long categoryId);
}
