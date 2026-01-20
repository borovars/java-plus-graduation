package ru.practicum.compilation.compilation_event;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CompilationEventId implements Serializable {
    private Long compilationId;
    private Long eventId;

    public Long getCompilationId() { return compilationId; }
    public void setCompilationId(Long compilationId) { this.compilationId = compilationId; }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
}
