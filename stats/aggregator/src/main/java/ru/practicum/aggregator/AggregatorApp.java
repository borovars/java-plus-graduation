package ru.practicum.aggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableDiscoveryClient
@ConfigurationPropertiesScan
public class AggregatorApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AggregatorApp.class, args);
        AggregatorStarter aggregatorStarter = context.getBean(AggregatorStarter.class);
        aggregatorStarter.start();
    }
}
