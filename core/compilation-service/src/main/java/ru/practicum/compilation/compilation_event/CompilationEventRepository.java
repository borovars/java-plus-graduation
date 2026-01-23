package ru.practicum.compilation.compilation_event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompilationEventRepository extends JpaRepository<CompilationEvent, CompilationEventId> {

    List<CompilationEvent> findAllByIdCompilationId(Long compilationId);

    void deleteAllByIdCompilationId(Long compilationId);
}
