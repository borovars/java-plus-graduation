package ru.practicum.feign.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.feign.category.dto.CategoryDto;
import ru.practicum.common.exception.NotFoundException;

public interface InternalCategoryContract {

    @GetMapping("/{categoryId}")
    CategoryDto findCategoryById(@PathVariable @NotNull @Positive Long categoryId) throws NotFoundException;
}
