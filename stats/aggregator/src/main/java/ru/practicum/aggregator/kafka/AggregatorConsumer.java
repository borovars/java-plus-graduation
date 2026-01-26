package ru.practicum.aggregator.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class AggregatorConsumer {

    @Value("${kafka.aggregator-deserializer}")
    private String AGGREGATOR_DESERIALIZER;

    @Value("${kafka.aggregator-group}")
    private String AGGREGATOR_GROUP;

    @Value("${kafka.bootstrap-server}")
    private String BOOTSTRAP_SERVER;

    @Value("${kafka.key-deserializer}")
    private String KEY_DESERIALIZER;

    @Bean
    public KafkaConsumer<String, SpecificRecordBase> createConsumer(){
        Map<String, Object> configProps = new HashMap<>();

        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KEY_DESERIALIZER);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, AGGREGATOR_DESERIALIZER);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, AGGREGATOR_GROUP);

        log.info("Создание консьюмера для аггрегатора");
        return new KafkaConsumer<>(configProps);
    }
}
