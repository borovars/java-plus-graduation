package ru.practicum.aggregator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.practicum.aggregator.kafka.AggregatorConsumer;
import ru.practicum.aggregator.kafka.AggregatorProducer;
import ru.yandex.practicum.ewm.stats.avro.UserActionAvro;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregatorStarter {

    private final AggregatorService service;
    private final AggregatorConsumer consumerFactory;
    private final AggregatorProducer producerFactory;

    @Value("${kafka.topics.user-action-topic}")
    private String USER_ACTION_TOPIC;

    @Value("${kafka.topics.similarity-topic}")
    private String SIMILARITY_TOPIC;

    public void start() {
        KafkaConsumer<String, SpecificRecordBase> consumer = consumerFactory.createConsumer();
        KafkaProducer<String, SpecificRecordBase> producer = producerFactory.createProducer();

        try {
            log.info("подписывается на топик действий");
            consumer.subscribe(List.of(USER_ACTION_TOPIC));

            while (true) {
                ConsumerRecords<String, SpecificRecordBase> records = consumer.poll(Duration.ofSeconds(1));

                records.forEach(record -> {
                    try {
                        UserActionAvro request = (UserActionAvro) record.value();
                        service.calculateSimilarity(request).forEach(result -> {
                            producer.send(new ProducerRecord<>(SIMILARITY_TOPIC, result));
                        });
                    } catch (Exception e) {
                        log.error("Ошибка обработки записи", e);
                    }
                });
                consumer.commitSync();
            }
        } catch (WakeupException e) {
            log.error("Ошибка WakeupException", e);
        } catch (Exception e) {
            log.error("Ошибка во время обработки", e);
        } finally {
            try {
                producer.flush();
                consumer.commitSync();
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
                log.info("Закрываем продюсер");
                producer.close();
            }
        }
    }
}