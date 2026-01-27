package ru.practicum.analyzer;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class AnalyzerConsumer {

    @Value("${kafka.bootstrap-server}")
    private String BOOTSTRAP_SERVER;

    @Value("${kafka.key-deserializer}")
    private String KEY_DESERIALIZER;

    @Value("${kafka.analyzer-deserializer}")
    private String ANALYZER_USER_DESERIALIZER;

    @Value("${kafka.analyzer-group-user}")
    private String ANALYZER_USER_GROUP;

    @Value("${kafka.analyzer-similarity-deserializer}")
    private String ANALYZER_SIMILARITY_DESERIALIZER;

    @Value("${kafka.analyzer-group-similarity}")
    private String ANALYZER_SIMILARITY_GROUP;


    @Bean
    public KafkaConsumer<String, SpecificRecordBase> createUserActionConsumer(){
        Map<String, Object> configProps = new HashMap<>();

        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KEY_DESERIALIZER);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ANALYZER_USER_DESERIALIZER);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, ANALYZER_USER_GROUP);

        log.info("Создание коньсьюмера для действий юзера в анализаторе");
        return new KafkaConsumer<>(configProps);
    }

    @Bean
    public KafkaConsumer<String, SpecificRecordBase> createSimilarityConsumer(){
        Map<String, Object> configProps = new HashMap<>();

        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KEY_DESERIALIZER);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ANALYZER_SIMILARITY_DESERIALIZER);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, ANALYZER_SIMILARITY_GROUP);

        log.info("Создание коньсьюмера для симилярити в анализаторе");
        return new KafkaConsumer<>(configProps);
    }
}
