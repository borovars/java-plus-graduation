package ru.practicum.event.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.event.EventRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class InternalEventService {

    private final EventRepository eventRepository;

    public boolean existsByCategoryId(Long categoryId){
        log.info("Запрос существования события с категорией {}", categoryId);
        return eventRepository.existsByCategoryId(categoryId);
    }
}
