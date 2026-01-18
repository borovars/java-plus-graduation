package ru.practicum.feign.event;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface InternalEventContract {

    @GetMapping
    boolean existsByCategoryId(@RequestParam @NotNull @Positive Long categoryId);
}
