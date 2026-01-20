package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.common.exception.AlreadyExistsException;
import ru.practicum.common.exception.BadArgumentsException;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.compilation.*;
import ru.practicum.compilation.compilation_event.CompilationEvent;
import ru.practicum.compilation.compilation_event.CompilationEventRepository;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.FullCompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationDto;
import ru.practicum.feign.event.EventFeignClient;
import ru.practicum.feign.event.dto.EventFullDto;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventFeignClient eventFeignClient;
    private final CompilationEventRepository compilationEventRepository;

    @Override
    @Transactional
    public FullCompilationDto addCompilation(NewCompilationDto dto) throws NotFoundException, AlreadyExistsException {
        log.info("Создание новой подборки: {}", dto.getTitle());

        Compilation compilation = CompilationMapper.toCompilation(dto);

        if (compilationRepository.existsByTitle(compilation.getTitle())) {
            throw new AlreadyExistsException("Подборка с таким именем уже существует");
        }

        // Сохраняем саму подборку (без событий)
        compilation = compilationRepository.save(compilation);

        // Добавлять события в подборку, если они указаны
        if (dto.getEvents() != null && !dto.getEvents().isEmpty()) {
            // Проверяем существование событий через Feign
            Set<Long> foundIds = eventFeignClient.findAllById(dto.getEvents());

            List<Long> missedEventIds = dto.getEvents().stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();

            if (!missedEventIds.isEmpty()) {
                throw new NotFoundException("Events with ids=" + missedEventIds + " was not found");
            }

            // Сохраняем связи в таблицу compilation_events
            Compilation finalCompilation = compilation;
            List<CompilationEvent> relations = foundIds.stream()
                    .map(eventId -> CompilationEvent.of(finalCompilation.getId(), eventId))
                    .collect(Collectors.toList());
            compilationEventRepository.saveAll(relations);

            // отобразим в transient поле для возврата DTO
            compilation.setEvents(new HashSet<>(foundIds));
        } else {
            compilation.setEvents(Collections.emptySet());
        }

        log.info("Подборка создана с id: {}", compilation.getId());
        FullCompilationDto full = CompilationMapper.toFullCompilationDto(compilation);
        full.setEvents(eventFeignClient.findAllByIdFull(compilation.getEvents().stream().toList()));
        return full;
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) throws NotFoundException {
        log.info("Удаление подборки с id: {}", compId);

        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException("Compilation with id=" + compId + " was not found");
        }

        // Сначала удаляем все связи
        compilationEventRepository.deleteAllByIdCompilationId(compId);

        // Затем удаляем саму подборку
        compilationRepository.deleteById(compId);
        log.info("Подборка с id {} удалена", compId);
    }

    @Override
    @Transactional
    public FullCompilationDto updateCompilation(Long compId, UpdateCompilationDto dto) throws NotFoundException, AlreadyExistsException {
        log.info("Обновление подборки с id: {}", compId);

        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));

        // Обновлять поля подборки, если они переданы
        if (dto.getTitle() != null) {
            if (!dto.getTitle().equals(compilation.getTitle()) && compilationRepository.existsByTitle(dto.getTitle())) {
                throw new AlreadyExistsException("Подборка с таким именем уже существует");
            }
            compilation.setTitle(dto.getTitle());
        }

        if (dto.getPinned() != null) {
            compilation.setPinned(dto.getPinned());
        }

        if (dto.getEvents() != null) {
            // проверяем все eventId, если хотя бы один не найден — бросаем исключение
            Set<Long> validIds = new HashSet<>();
            for (Long eventId : dto.getEvents()) {
                if (eventFeignClient.existsById(eventId)) {
                    validIds.add(eventId);
                } else {
                    throw new NotFoundException("Event with id=" + eventId + " was not found");
                }
            }

            // Удаляем старые связи
            compilationEventRepository.deleteAllByIdCompilationId(compId);

            // Вставляем новые связи
            List<CompilationEvent> newRelations = validIds.stream()
                    .map(eid -> CompilationEvent.of(compId, eid))
                    .collect(Collectors.toList());
            compilationEventRepository.saveAll(newRelations);

            // отобразим в transient поле
            compilation.setEvents(validIds);
        } else {
            // если events == null — не меняем связи; если требуется явно очистить, отправлять пустой список из клиента
        }

        compilation = compilationRepository.save(compilation);
        log.info("Подборка с id {} обновлена", compId);
        FullCompilationDto full = CompilationMapper.toFullCompilationDto(compilation);
        full.setEvents(eventFeignClient.findAllByIdFull(compilation.getEvents().stream().toList()));

        return full;
    }

    @Override
    @Transactional(readOnly = true)
    public FullCompilationDto getCompilationById(Long compId) throws NotFoundException {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка с id " + compId + " не найдена"));

        // Получаем ids событий из таблицы связей
        List<CompilationEvent> relations = compilationEventRepository.findAllByIdCompilationId(compId);
        Set<Long> eventIds = relations.stream()
                .map(CompilationEvent::getEventId)
                .collect(Collectors.toSet());

        // Запрашиваем полные DTO событий через feign (если нужно), иначе пустой набор
        Set<EventFullDto> eventsFull = eventIds.isEmpty() ? Collections.emptySet() : eventFeignClient.findAllByIdFull(eventIds.stream().toList());

        FullCompilationDto fullDto = CompilationMapper.toFullCompilationDto(compilation);
        fullDto.setEvents(eventsFull);

        return fullDto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FullCompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) throws
            BadArgumentsException {

        if (from < 0 || size <= 0) {
            throw new BadArgumentsException("Неверные параметры пагинации");
        }

        Pageable pageable = PageRequest.of(from / size, size);

        Page<Compilation> page;
        if (pinned != null) {
            page = compilationRepository.findAllByPinned(pinned, pageable);
        } else {
            page = compilationRepository.findAll(pageable);
        }

        Page<FullCompilationDto> dtoPage = page.map(comp -> {
            List<CompilationEvent> relations =
                    compilationEventRepository.findAllByIdCompilationId(comp.getId());

            Set<Long> eventIds = relations.stream()
                    .map(CompilationEvent::getEventId)
                    .collect(Collectors.toSet());

            comp.setEvents(eventIds);

            FullCompilationDto full = CompilationMapper.toFullCompilationDto(comp);

            full.setEvents(
                    eventFeignClient.findAllByIdFull(eventIds.stream().toList())
            );

            return full;
        });
        return dtoPage;
    }
}