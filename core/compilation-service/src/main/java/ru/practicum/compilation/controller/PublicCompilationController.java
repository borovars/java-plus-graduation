package ru.practicum.compilation.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.exception.BadArgumentsException;
import ru.practicum.common.exception.NotFoundException;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.FullCompilationDto;
import ru.practicum.compilation.service.CompilationService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class PublicCompilationController {

    private final CompilationService compilationService;

    @GetMapping("/{compId}")
    public FullCompilationDto getCompilationById(@PathVariable(name = "compId") long compId) throws NotFoundException {
        return compilationService.getCompilationById(compId);
    }

    @GetMapping
    public List<FullCompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(defaultValue = "10") @Positive Integer size,
                                                HttpServletResponse response) throws BadArgumentsException {
        Page<FullCompilationDto> page = compilationService.getCompilations(pinned, from, size);
        response.setHeader("X-Total-Count", String.valueOf(page.getTotalElements()));

        return page.getContent();
    }
}
