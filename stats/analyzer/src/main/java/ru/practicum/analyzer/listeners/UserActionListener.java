package ru.practicum.analyzer.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.practicum.analyzer.service.UserActionService;
import ru.yandex.practicum.ewm.stats.avro.UserActionAvro;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserActionListener {

    private final UserActionService userActionService;

    @KafkaListener(
            topics = "${kafka.topics.user-action-topic}",
            containerFactory = "kafkaListenerContainerFactoryUserAction"
    )
    public void onMessage(SpecificRecordBase message) {
        try {
            UserActionAvro action = (UserActionAvro) message;
            userActionService.saveUserAction(action);
            log.debug("UserActionListener прочитал сообщение {}", action.toString());
        } catch (Exception e) {
            log.error("Ошибка обработки user action", e);
        }
    }
}