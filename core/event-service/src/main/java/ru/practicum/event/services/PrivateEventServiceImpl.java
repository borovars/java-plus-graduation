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
import ru.practicum.StatsClient;
import ru.practicum.category.Category;
import ru.practicum.category.service.CategoryService;
import ru.practicum.common.exception.ConflictException;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.dto.StatsDto;
import ru.practicum.event.Event;
import ru.practicum.event.EventMapper;
import ru.practicum.event.EventRepository;
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
import ru.practicum.location.Location;
import ru.practicum.location.LocationMapper;
import ru.practicum.location.LocationService;
import ru.practicum.feign.user.UserFeignClient;
import ru.practicum.feign.user.dto.UserDto;

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
    private final StatsClient statsClient;

    private final EventRepository eventRepository;

    @Override
    public Page<EventShortDto> getEventsByUserId(long userId, int from, int size) throws NotFoundException {
        log.info("Запрос списка событий, созданных пользователем на уровне сервиса");

        UserDto userDto = userFeignClient.findByUserId(userId);
        log.info("Передан идентификатор инициатора событий: {}", userDto.getId());

        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Direction.ASC, "id"));

        Page<Event> searchResult = eventRepository.findAllByInitiatorId(userDto.getId(), pageRequest);
        log.info("Из хранилища получена коллекция размером {}", searchResult.getTotalElements());

        List<Event> events = searchResult.getContent();

        Map<Long, Long> views = getAmountOfViews(events);

        List<EventShortDto> dtoList = events.stream()
                .map(EventMapper::mapToEventShortDto)
                .peek(eventShortDto -> eventShortDto.setViews(views.getOrDefault(eventShortDto.getId(), 0L)))
                .collect(Collectors.toList());

        log.info("Полученная коллекция преобразована. Размер коллекции после преобразования {}", dtoList.size());

        log.info("Возврат результатов поиска на уровень контроллера");
        return new PageImpl<>(dtoList, pageRequest, searchResult.getTotalElements());
    }

    @Override
    @Transactional
    public EventFullDto createEvent(long userId, EventCreateDto dto) throws NotFoundException, ConflictException {
        log.info("Создание события на уровне сервиса");

        UserDto user = userFeignClient.findByUserId(userId);
        log.info("Передан идентификатор инициатора: {}", user.getId());

        CategoryDto category = categoryFeignClient.findCategoryById(dto.getCategory());
        log.info("Передан идентификатор категории: {}", category.getId());

        Event event = EventMapper.mapToEvent(dto);
        event.setCategory(category.getId());

        if (dto.getLocation() != null) {
            LocationDto location = dto.getLocation();
            Long locationId = locationFeignClient.save(location);
            event.setLocation(locationId);
        }

        event.setInitiator(user.getId());
        log.info("Несохраненная модель преобразована");

        log.info("Валидация несохраненной модели");
        validateEvent(event);
        log.info("Валидация несохраненной модели завершена");

        event = eventRepository.save(event);
        log.info("Сохранение модели завершено. Получен идентификатор {}", event.getId());

        EventFullDto result = EventMapper.mapToFullDto(event);

        completeModel(result, event);
        log.info("Сохраненная модель преобразована. Идентификатор модели после преобразования {}", result.getId());

        log.info("Возврат результатов создания пользователя на уровень контроллера");
        return result;
    }

    @Override
    public EventFullDto getEventByUserIdAndEventId(long userId, long eventId) throws NotFoundException,
            ConflictException {
        log.info("Поиск полной информации о событии на уровне сервиса");

        UserDto user = userFeignClient.findByUserId(userId);
        log.info("Передан идентификатор инициатора события: {}", user.getId());

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        log.info("Передан идентификатор события: {}", event.getId());

        if (!user.getId().equals(event.getInitiator())) {
            throw new ConflictException(
                    "User with id=" + user.getId() + " is not initiator of event with id=" + event.getId());
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

        UserDto userDto = userFeignClient.findByUserId(userId);
        log.info("Передан идентификатор пользователя: {}", userDto.getId());

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("User with id=" + eventId + " was not found"));
        log.info("Передан идентификатор обновляемого события: {}", event.getId());

        if (!event.getInitiator().equals(userDto.getId())) {
            throw new ConflictException(
                    "User with id=" + userDto.getId() + " is not initiator of event with id=" + event.getId());
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

        log.info("Заполнение количества одобренных заявок");
        eventFullDto.setConfirmedRequests(event.getConfirmedRequests());
        log.info("Заполнение количества одобренных заявок завершено");

        log.info("Заполнение количества просмотров события");
        Map<Long, Long> views = getAmountOfViews(List.of(event));
        eventFullDto.setViews(views.getOrDefault(event.getId(), 0L));
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

    private Map<Long, Long> getAmountOfViews(List<Event> events) {
        if (events == null || events.isEmpty()) {
            return Collections.emptyMap();
        }

        List<String> uris = events.stream()
                .map(event -> "/events/" + event.getId())
                .distinct()
                .collect(Collectors.toList());

        LocalDateTime startTime = events.stream()
                .map(Event::getCreatedOn)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now().minusYears(1));
        LocalDateTime endTime = LocalDateTime.now();

        Map<Long, Long> viewsMap = new HashMap<>();
        try {
            log.info("Получение статистики по времени для URI: {} c {} по {}", uris, startTime, endTime);

            log.debug("Вызов StatsClient.getStats c параметрами {},{},{},{}", startTime, endTime, uris, true);
            List<StatsDto> stats = statsClient.getStats(startTime, endTime, uris, true);
            log.debug("StatsClient вернул {}", stats);
            if (stats == null || stats.isEmpty()) {
                log.info("Сервис статистики вернул пустой список");
                return Collections.emptyMap();
            }

            for (StatsDto s : stats) {
                String uri = s.getUri();
                Long hits = s.getHits() != null ? s.getHits() : 0L;
                Long eventId = Long.parseLong(uri.substring("/events/".length()));

                viewsMap.put(eventId, hits);
            }
        } catch (Exception e) {
            log.debug("Ошибка при получении статистики просмотров");
        }
        return viewsMap;
    }
}
