package ru.practicum.compilation.compilation_event;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "compilation_events")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CompilationEvent {

    @EmbeddedId
    private CompilationEventId id;

    public static CompilationEvent of(Long compilationId, Long eventId) {
        return CompilationEvent.builder()
                .id(new CompilationEventId(compilationId, eventId))
                .build();
    }

    public Long getCompilationId() {
        return id != null ? id.getCompilationId() : null;
    }

    public Long getEventId() {
        return id != null ? id.getEventId() : null;
    }
}