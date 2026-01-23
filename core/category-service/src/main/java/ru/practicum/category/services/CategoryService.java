package ru.practicum.category.services;

import org.springframework.data.domain.Page;
import ru.practicum.feign.category.dto.CategoryDto;
import ru.practicum.feign.category.dto.NewCategoryDto;
import ru.practicum.common.exception.AlreadyExistsException;
import ru.practicum.common.exception.ConflictException;
import ru.practicum.common.exception.NotFoundException;

public interface CategoryService {

    CategoryDto addCategory(NewCategoryDto dto) throws AlreadyExistsException;

    Page<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoryById(Long id) throws NotFoundException;

    CategoryDto updateCategory(Long id, NewCategoryDto dto) throws NotFoundException, AlreadyExistsException;

    void deleteCategory(Long id) throws NotFoundException, ConflictException;

    CategoryDto findById(Long id) throws NotFoundException;
}