package ru.practicum.compilation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CompilationApp {
    public static void main(String[] args) {
        SpringApplication.run(CompilationApp.class, args);
    }
}
