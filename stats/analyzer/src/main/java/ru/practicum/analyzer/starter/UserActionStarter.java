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
import ru.practicum.analyzer.service.UserActionService;
import ru.yandex.practicum.ewm.stats.avro.UserActionAvro;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserActionStarter implements Runnable {

    private final UserActionService userActionService;
    private final AnalyzerConsumer analyzerConsumer;

    @Value("${kafka.topics.user-action-topic}")
    private String USER_ACTION_TOPIC;

    @Override
    public void run() {
        KafkaConsumer<String, SpecificRecordBase> consumer = analyzerConsumer.createUserActionConsumer();

        try {
            consumer.subscribe(List.of(USER_ACTION_TOPIC));

            while (true) {
                ConsumerRecords<String, SpecificRecordBase> records = consumer.poll(Duration.ofSeconds(1));
                log.debug("Получено {} записей из топика {}", records.count(), USER_ACTION_TOPIC);

                records.forEach(record -> {
                    try {
                        UserActionAvro request = (UserActionAvro) record.value();
                        userActionService.saveUserAction(request);
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