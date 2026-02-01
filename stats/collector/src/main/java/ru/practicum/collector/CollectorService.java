package ru.practicum.collector;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.proto.UserActionProto;
import ru.yandex.practicum.ewm.stats.avro.UserActionAvro;

@RequiredArgsConstructor
@Slf4j
@Service
public class CollectorService {

    private final KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;
    private final CollectorMapper mapper;

    @Value("${kafka.topics.user-action-topic}")
    private String userActionTopic;

    public void createUserAction(UserActionProto request) {
        log.info("Запрос на создание действия для рекомендаций: {}", request);

        UserActionAvro avro = mapper.mapToAvro(request);

        kafkaTemplate.send(userActionTopic, avro)
                .whenComplete((result, exception) -> {
                    if (exception == null) {
                        log.debug("Collector отправил в топик {} сообщение {}", userActionTopic, avro.toString());
                        log.info("Действие успешно отправлено в топик {}", userActionTopic);
                    } else {
                        log.error("Не удалось отправить действие в топик {}", userActionTopic, exception);
                    }
                });
    }
}
