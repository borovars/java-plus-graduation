package ru.practicum.analyzer.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.practicum.analyzer.service.SimilarityService;
import ru.yandex.practicum.ewm.stats.avro.EventSimilarityAvro;

@Slf4j
@Component
@RequiredArgsConstructor
public class SimilarityListener {

    private final SimilarityService similarityService;

    @KafkaListener(
            topics = "${kafka.topics.similarity-topic}",
            containerFactory = "kafkaListenerContainerFactorySimilarity"
    )
    public void onMessage(SpecificRecordBase message) {
        try {
            EventSimilarityAvro similarity = (EventSimilarityAvro) message;
            similarityService.saveSimilarity(similarity);
            log.debug("SimilarityListener прочитал сообщение {}", similarity.toString());
        } catch (Exception e) {
            log.error("Ошибка обработки similarity", e);
        }
    }
}