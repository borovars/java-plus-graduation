package ru.practicum.event.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import ru.practicum.common.exception.ConflictException;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.event.Event;
import ru.practicum.event.EventMapper;
import ru.practicum.event.EventRepository;
import ru.practicum.ewm.stats.proto.RecommendedEventProto;
import ru.practicum.feign.category.CategoryFeignClient;
import ru.practicum.feign.category.dto.CategoryDto;
import ru.practicum.feign.event.dto.EventCreateDto;
import ru.practicum.feign.event.dto.EventFullDto;
import ru.practicum.feign.event.dto.EventShortDto;
import ru.practicum.feign.event.dto.EventUpdateDto;
import ru.practicum.feign.event.enums.StateActions;
import ru.practicum.feign.event.enums.States;
import ru.practicum.event.services.interfaces.PrivateEventService;
import ru.practicum.feign.location.LocationFeignClient;
import ru.practicum.feign.location.dto.LocationDto;
import ru.practicum.feign.user.UserFeignClient;
import ru.practicum.stats.AnalyzerClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrivateEventServiceImpl implements PrivateEventService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final UserFeignClient userFeignClient;
    private final CategoryFeignClient categoryFeignClient;
    private final LocationFeignClient locationFeignClient;
    private final AnalyzerClient analyzerClient;
    private final EventRepository eventRepository;

    @Override
    public List<EventShortDto> getEventsByUserId(long userId, int from, int size) throws NotFoundException {
        log.info("Запрос списка событий, созданных пользователем на уровне сервиса");

        if (!userFeignClient.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }

        log.info("Передан идентификатор инициатора событий: {}", userId);

        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Direction.ASC, "id"));

        Page<Event> searchResult = eventRepository.findAllByInitiator(userId, pageRequest);
        log.info("Из хранилища получена коллекция размером {}", searchResult.getTotalElements());

        return searchResult.getContent().stream()
                        .map(event -> {
                            EventShortDto eventShortDto = EventMapper.mapToEventShortDto(event);
                            eventShortDto.setRating(analyzerClient.getInteractionsCount(List.of(event.getId()))
                                    .map(RecommendedEventProto::getScore)
                                    .findFirst()
                                    .orElse(0.0));
                            return eventShortDto;
                        }).toList();
    }

    @Override
    @Transactional
    public EventFullDto createEvent(long userId, EventCreateDto dto) throws NotFoundException, ConflictException {
        log.info("Создание события на уровне сервиса");

        if (!userFeignClient.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }

        log.info("Передан идентификатор инициатора: {}", userId);

        CategoryDto category = categoryFeignClient.findCategoryById(dto.getCategory());
        log.info("Передан идентификатор категории: {}", category.getId());

        Event event = EventMapper.mapToEvent(dto);
        event.setCategory(category.getId());
        event.setInitiator(userId);

        if (dto.getLocation() != null) {
            LocationDto location = dto.getLocation();
            Long locationId = locationFeignClient.save(location);
            event.setLocation(locationId);
        }

        validateEvent(event);

        event = eventRepository.save(event);
        log.info("Событие сохранено с ID {}", event.getId());

        EventFullDto result = EventMapper.mapToFullDto(event);

        completeModel(result, event);

        if (event.getLocation() != null) {
            LocationDto locationDto = locationFeignClient.get(event.getLocation());
            result.setLocation(locationDto);
        }

        log.info("Создано полное событие с ID {}", result.getId());
        return result;
    }

    @Override
    public EventFullDto getEventByUserIdAndEventId(long userId, long eventId) throws NotFoundException,
            ConflictException {
        log.info("Поиск полной информации о событии на уровне сервиса");

        if (!userFeignClient.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }

        log.info("Передан идентификатор инициатора события: {}", userId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        log.info("Передан идентификатор события: {}", event.getId());

        if (userId != event.getInitiator()) {
            throw new ConflictException(
                    "User with id=" + userId + " is not initiator of event with id=" + event.getId());
        }

        EventFullDto result = EventMapper.mapToFullDto(event);
        completeModel(result, event);
        log.info("Полученная модель преобразована. Идентификатор модели после преобразования {}", result.getId());

        log.info("Возврат полной информации о событии на уровень контроллера");
        return result;
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(long userId, long eventId, EventUpdateDto dto) throws NotFoundException,
            ConflictException {
        log.info("Обновление события на уровне сервиса");

        if (!userFeignClient.existsById(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }

        log.info("Передан идентификатор пользователя: {}", userId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("User with id=" + eventId + " was not found"));
        log.info("Передан идентификатор обновляемого события: {}", event.getId());

        if (!event.getInitiator().equals(userId)) {
            throw new ConflictException(
                    "User with id=" + userId + " is not initiator of event with id=" + event.getId());
        }

        if (event.getState() == States.PUBLISHED) {
            throw new ConflictException("Невозможно обновить опубликованное событие");
        }

        changeEventState(event, dto);

        EventMapper.updateFields(event, dto);
        log.info("Обновляемая модель дополнена данными");

        log.info("Валидация обновленной модели");
        validateEvent(event);
        log.info("Валидация обновленной модели завершена");

        event = eventRepository.save(event);
        log.info("Изменения модели сохранены");

        EventFullDto result = EventMapper.mapToFullDto(event);

        completeModel(result, event);
        log.info("Измененная модель преобразована. Идентификатор модели после преобразования {}", result.getId());

        log.info("Возврат результатов обновления события на уровень контроллера");
        return result;
    }

    /**
     * Метод заполняет переданную модель события
     *
     * @param event событие
     */
    private void completeModel(EventFullDto eventFullDto, Event event) {
        log.info("Заполнение события");

        log.info("Заполнение рейтинга события");

        eventFullDto.setRating(analyzerClient.getInteractionsCount(List.of(event.getId()))
                .map(RecommendedEventProto::getScore)
                .findFirst()
                .orElse(0.0));

        log.info("Заполнение количества просмотров события завершено");

        log.info("Заполнение события завершено");
    }

    /**
     * Метод проверяет правильность заполнения полей события
     *
     * @param event событие
     * @throws ConflictException если нарушены ограничения по дате события
     */
    private void validateEvent(Event event) throws ConflictException {
        log.info("Валидация даты события");
        validateEventDate(event.getEventDate());
        log.info("Валидация даты события завершена");
    }

    /**
     * Метод проверяет правильность заполнения даты события
     *
     * @param eventDate дата события
     * @throws ConflictException если нарушены ограничения по дате события
     */
    private void validateEventDate(LocalDateTime eventDate) throws ConflictException {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException(
                    "Field: eventDate. Error: должно содержать дату, которая не раньше, чем через 2 часа. Value: "
                            + eventDate.plusHours(2).format(DATE_TIME_FORMATTER));
        }
    }

    private void changeEventState(Event event, EventUpdateDto update) {
        if (update.getStateAction() != null) {
            if (update.getStateAction() == StateActions.SEND_TO_REVIEW) {
                event.setState(States.PENDING);
            }
            if (update.getStateAction() == StateActions.CANCEL_REVIEW) {
                event.setState(States.CANCELED);
            }
        }
    }
}
