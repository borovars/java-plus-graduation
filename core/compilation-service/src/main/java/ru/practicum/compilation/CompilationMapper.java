package ru.practicum.compilation;

import lombok.experimental.UtilityClass;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.FullCompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;

import java.util.Set;


@UtilityClass
public class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto dto) {
        if (dto == null) return null;
        return Compilation.builder()
                .title(dto.getTitle())
                .pinned(dto.getPinned() != null ? dto.getPinned() : false)
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        CompilationDto dto = new CompilationDto();
        dto.setId(compilation.getId());
        dto.setTitle(compilation.getTitle());
        dto.setPinned(compilation.getPinned());
        return dto;
    }

    public static FullCompilationDto toFullCompilationDto(Compilation compilation) {
        if (compilation == null) return null;
        FullCompilationDto dto = new FullCompilationDto();
        dto.setId(compilation.getId());
        dto.setTitle(compilation.getTitle());
        dto.setPinned(compilation.getPinned());
        return dto;
    }
}