package ru.practicum.event.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.event.Event;
import ru.practicum.event.EventMapper;
import ru.practicum.event.EventRepository;
import ru.practicum.ewm.stats.proto.RecommendedEventProto;
import ru.practicum.feign.event.dto.EventFullDto;
import ru.practicum.feign.event.dto.EventShortDto;
import ru.practicum.feign.event.enums.States;
import ru.practicum.feign.request.RequestFeignClient;
import ru.practicum.stats.AnalyzerClient;
import ru.practicum.stats.CollectorClient;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublicEventService {

    private final EventRepository eventRepository;
    private final AnalyzerClient analyzerClient;
    private final CollectorClient collectorClient;
    private final RequestFeignClient requestFeignClient;

    @Transactional(readOnly = true)
    public Page<EventShortDto> getEventsWithFilters(String text, List<Long> categories, Boolean paid,
                                                    LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                    Boolean onlyAvailable, String sort, Integer from,
                                                    Integer size, HttpServletRequest request) throws BadRequestException {
        log.info("Получен запрос от публичного юзера на получение событий с фильтрами");
        if ((rangeStart != null) && (rangeEnd != null) && (rangeStart.isAfter(rangeEnd))) {
            throw new BadRequestException("Время начала не может быть позже окончания");
        }

        int page = from / size;
        Page<Event> events = eventRepository.findAllByFiltersPublic(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, PageRequest.of(page, size));

        return events.map(event -> {
            EventShortDto dto = EventMapper.mapToEventShortDto(event);
            dto.setRating(analyzerClient.getInteractionsCount(List.of(event.getId()))
                    .map(RecommendedEventProto::getScore)
                    .findFirst()
                    .orElse(0.0));
            dto.setConfirmedRequests(requestFeignClient.findConfirmedRequests(event.getId()));
            return dto;
        });
    }

    public EventFullDto getEventById(Long eventId, HttpServletRequest request, Long userId) throws NotFoundException, BadRequestException {
        log.info("Получен запрос от публичного юзера на получение полной информации о событии с id {}", eventId);

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Событие с id " + eventId + " не найдено"));

        if (event.getState() != States.PUBLISHED) {
            throw new NotFoundException("Событие с id " + eventId + " недоступно, так как не опубликовано");
        }

        collectorClient.collectUserAction(userId, eventId, "ACTION_VIEW", Instant.now());

        EventFullDto eventFullDto = EventMapper.mapToFullDto(event);
        return eventFullDto;
    }

    @Transactional(readOnly = true)
    public List<EventShortDto> getRecommendations(Long max, Long userId){
        List<Long> eventIds = analyzerClient.getRecommendationsForUser(userId, max)
                .map(RecommendedEventProto::getEventId)
                .toList();
        List<Event> events = eventRepository.findAllById(eventIds);

        return events.stream()
                .map(event -> {
                    EventShortDto eventShortDto = EventMapper.mapToEventShortDto(event);
                    eventShortDto.setRating(analyzerClient.getInteractionsCount(List.of(event.getId()))
                            .map(RecommendedEventProto::getScore)
                            .findFirst()
                            .orElse(0.0));
                    return eventShortDto;
                })
                .toList();
    }

    public void addLike(Long eventId, Long userId) throws NotFoundException {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено"));
        if (requestFeignClient.checkRegistration(eventId, userId)) {
            collectorClient.collectUserAction(userId, eventId, "ACTION_LIKE", Instant.now());
        } else {
            throw new NotFoundException("Пользователь не регистрировался на данное событие");
        }
    }
}
