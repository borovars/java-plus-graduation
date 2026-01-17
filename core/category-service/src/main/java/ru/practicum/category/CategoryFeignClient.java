package ru.practicum.category;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "category-service")
public class CategoryFeignClient {

}
