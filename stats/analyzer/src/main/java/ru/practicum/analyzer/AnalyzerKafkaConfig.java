package ru.practicum.analyzer;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class AnalyzerKafkaConfig {

    @Value("${kafka.bootstrap-server}")
    private String bootstrapServers;

    @Value("${kafka.key-deserializer}")
    private String keyDeserializer;

    @Value("${kafka.analyzer-deserializer}")
    private String userActionDeserializer;

    @Value("${kafka.analyzer-similarity-deserializer}")
    private String similarityDeserializer;

    @Value("${kafka.analyzer-group-user}")
    private String userGroupId;

    @Value("${kafka.analyzer-group-similarity}")
    private String similarityGroupId;


    @Bean
    public ConsumerFactory<String, SpecificRecordBase> userActionConsumerFactory() {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, userActionDeserializer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, userGroupId);

        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SpecificRecordBase>
    kafkaListenerContainerFactoryUserAction() {

        ConcurrentKafkaListenerContainerFactory<String, SpecificRecordBase> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(userActionConsumerFactory());
        factory.setConcurrency(1);

        return factory;
    }

    @Bean
    public ConsumerFactory<String, SpecificRecordBase> similarityConsumerFactory() {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, similarityDeserializer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, similarityGroupId);

        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SpecificRecordBase>
    kafkaListenerContainerFactorySimilarity() {

        ConcurrentKafkaListenerContainerFactory<String, SpecificRecordBase> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(similarityConsumerFactory());
        factory.setConcurrency(1);

        return factory;
    }
}
