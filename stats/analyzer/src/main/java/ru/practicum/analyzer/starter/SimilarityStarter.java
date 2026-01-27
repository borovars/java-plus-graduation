package ru.practicum.analyzer.starter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.practicum.analyzer.AnalyzerConsumer;
import ru.practicum.analyzer.service.SimilarityService;
import ru.yandex.practicum.ewm.stats.avro.EventSimilarityAvro;

import java.time.Duration;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SimilarityStarter implements Runnable {

    private final SimilarityService similarityService;
    private AnalyzerConsumer analyzerConsumer;

    @Value("${kafka.topics.similarity-topic}")
    private String SIMILARITY_TOPIC;

    @Override
    public void run(){
        KafkaConsumer<String, SpecificRecordBase> consumer = analyzerConsumer.createSimilarityConsumer();

        try {
            consumer.subscribe(List.of(SIMILARITY_TOPIC));

            while (true){
                ConsumerRecords<String, SpecificRecordBase> records = consumer.poll(Duration.ofSeconds(1));
                log.debug( "Получено {} записей из топика {}", records.count(), SIMILARITY_TOPIC);

                records.forEach(record -> {
                    try {
                        EventSimilarityAvro request = (EventSimilarityAvro) record.value();
                        similarityService.saveSimilarity(request);
                    } catch (Exception e) {
                        log.error("Ошибка обработки записи", e);
                    }
                });

                consumer.commitSync();
            }
        } catch (WakeupException ignored) {
            log.error("Ошибка WakeupException");
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий", e);
        } finally {
            try {
                consumer.commitSync();
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
            }
        }
    }
}
