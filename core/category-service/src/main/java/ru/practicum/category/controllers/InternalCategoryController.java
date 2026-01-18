package ru.practicum.category.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.feign.category.InternalCategoryContract;
import ru.practicum.feign.category.dto.CategoryDto;
import ru.practicum.category.services.CategoryService;
import ru.practicum.common.exception.NotFoundException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/internal/category")
public class InternalCategoryController implements InternalCategoryContract {

    private final CategoryService categoryService;

    @Override
    public CategoryDto findCategoryById(Long categoryId) throws NotFoundException {
        return categoryService.findById(categoryId);
    }
}
