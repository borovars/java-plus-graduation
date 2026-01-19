package ru.practicum.event.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.event.Event;
import ru.practicum.event.EventMapper;
import ru.practicum.event.EventRepository;
import ru.practicum.feign.event.dto.EventFullDto;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InternalEventService {

    private final EventRepository eventRepository;

    public boolean existsByCategoryId(Long categoryId) {
        log.info("Запрос существования события с категорией {}", categoryId);
        return eventRepository.existsByCategoryId(categoryId);
    }

    public Set<Long> findAllById(Set<Long> eventsIds) {
        log.info("Запрос событий с id {}", eventsIds);
        return eventRepository.findAllById(eventsIds)
                .stream()
                .map(Event::getId)
                .collect(Collectors.toSet());
    }

    public boolean existsById(Long eventId) throws NotFoundException {
        log.info("Запрос существования события с id {}", eventId);

        if (eventRepository.existsById(eventId)) {
            return true;
        } else {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
    }

    public EventFullDto findEventById(Long eventId) throws NotFoundException {
        log.info("Запрос полного dto для события с id {}", eventId);
        return EventMapper.mapToFullDto(eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " was not found")
        ));
    }
}
