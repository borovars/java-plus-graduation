package ru.practicum.event;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages ={"ru.practicum.feign.category", "ru.practicum.feign.user",
        "ru.practicum.feign.location", "ru.practicum.feign.compilation", "ru.practicum.feign.request",
        "ru.practicum.feign.stats"})
@ConfigurationPropertiesScan
public class EventApp {
    public static void main(String[] args) {
        SpringApplication.run(EventApp.class, args);
    }
}
