package ru.practicum.aggregator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.yandex.practicum.ewm.stats.avro.UserActionAvro;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregatorStarter {

    private final AggregatorService service;
    private final KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;

    @Value("${kafka.topics.similarity-topic}")
    private String similarityTopic;

    @KafkaListener(
            topics = "${kafka.topics.user-action-topic}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void onMessage(UserActionAvro request) {
        try {
            log.debug("Получено событие UserAction: {}", request);

            List<EventSimilarityAvro> similarities =
                    service.calculateSimilarity(request);

            similarities.forEach(similarity -> {
                        kafkaTemplate.send(similarityTopic, similarity);
                        log.debug("Aggregator отправил в топик {} сообщение: {}", similarityTopic, similarity.toString());
                    }
            );

        } catch (Exception e) {
            log.error("Ошибка обработки UserAction", e);
        }
    }
}