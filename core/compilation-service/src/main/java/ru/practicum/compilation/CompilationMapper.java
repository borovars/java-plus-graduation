package ru.practicum.compilation;

import lombok.experimental.UtilityClass;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;

import java.util.Collections;

@UtilityClass
public class CompilationMapper {

    public Compilation toCompilation(NewCompilationDto dto) {
        return Compilation.builder()
                .title(dto.getTitle())
                .pinned(dto.getPinned())
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(compilation.getEvents() != null ?
                        compilation.getEvents() :
                        Collections.emptySet())
                .build();
    }
}