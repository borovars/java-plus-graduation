package ru.practicum.aggregator.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class AggregatorProducer {

    @Value("${kafka.bootstrap-server}")
    private String BOOTSTRAP_SERVER;

    @Value("${kafka.key-serializer}")
    private String KEY_SERIALIZER;

    @Value("${kafka.value-serializer}")
    private String VALUE_SERIALIZER;

    @Bean
    public KafkaProducer<String, SpecificRecordBase> createProducer() {
        Map<String, Object> configProps = new HashMap<>();

        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KEY_SERIALIZER);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, VALUE_SERIALIZER);

        log.info("Создание продюсера для аггрегатора");
        return new KafkaProducer<>(configProps);
    }
}
