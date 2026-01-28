package ru.practicum.event;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "ru.practicum")
@EnableFeignClients(basePackages = "ru.practicum.feign")
@ConfigurationPropertiesScan
public class EventApp {
    public static void main(String[] args) {
        SpringApplication.run(EventApp.class, args);
    }
}
