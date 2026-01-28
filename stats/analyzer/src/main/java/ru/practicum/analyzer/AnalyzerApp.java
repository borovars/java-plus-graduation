package ru.practicum.analyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import ru.practicum.analyzer.starter.SimilarityStarter;
import ru.practicum.analyzer.starter.UserActionStarter;

@SpringBootApplication
@EnableDiscoveryClient
@ConfigurationPropertiesScan
public class AnalyzerApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AnalyzerApp.class, args);

        SimilarityStarter similarityProcessor = context.getBean(SimilarityStarter.class);
        UserActionStarter userActionProcessor = context.getBean(UserActionStarter.class);

        Thread userActionsThread = new Thread(userActionProcessor);
        userActionsThread.setName("UserActionHandlerThread");
        userActionsThread.start();

        similarityProcessor.run();
    }
}
