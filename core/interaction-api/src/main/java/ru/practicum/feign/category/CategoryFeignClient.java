package ru.practicum.feign.category;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "category-service", path = "/internal/category")
public interface CategoryFeignClient extends InternalCategoryContract {
}
