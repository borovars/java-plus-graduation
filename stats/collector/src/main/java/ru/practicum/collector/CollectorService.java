package ru.practicum.collector;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.proto.UserActionProto;
import ru.yandex.practicum.ewm.stats.avro.UserActionAvro;

@RequiredArgsConstructor
@Slf4j
@Service
public class CollectorService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaProducer kafkaProducer;
    private final CollectorMapper mapper;

    @Value("${kafka.topics.user-action-topic}")
    private String USER_ACTION_TOPIC;

    public void createUserAction(UserActionProto request){
        log.info("Запрос на создание действия для рекомендаций");

        UserActionAvro avro = mapper.mapToAvro(request);

        kafkaTemplate.send(USER_ACTION_TOPIC, avro)
                .whenComplete((result, exception) -> {
                    if (exception == null){
                        log.info("Действие успешно отправлено");
                    } else {
                        log.error("Не удалось отправить действие");
                    }
                });
    }
}
