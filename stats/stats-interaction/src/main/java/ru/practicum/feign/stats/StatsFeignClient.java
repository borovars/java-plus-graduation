package ru.practicum.feign.stats;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "stats-service", path = "/internal/stats")
public interface StatsFeignClient extends InternalStatsContract {
}