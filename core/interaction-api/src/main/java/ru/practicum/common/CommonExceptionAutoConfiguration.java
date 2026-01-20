package ru.practicum.common;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
@ComponentScan(basePackages = {"ru.practicum.common.exception", "ru.practicum.common.config"})
public class CommonExceptionAutoConfiguration {
}