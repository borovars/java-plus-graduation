package ru.practicum;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(StatsClientProperties.class)
public class StatsClientAutoConfiguration {
}