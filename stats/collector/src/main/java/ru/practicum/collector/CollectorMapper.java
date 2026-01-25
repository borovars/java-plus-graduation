package ru.practicum.collector;


import com.google.protobuf.Timestamp;
import org.mapstruct.Mapper;
import ru.practicum.ewm.stats.proto.ActionTypeProto;
import ru.practicum.ewm.stats.proto.UserActionProto;
import ru.yandex.practicum.ewm.stats.avro.ActionTypeAvro;
import ru.yandex.practicum.ewm.stats.avro.UserActionAvro;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface CollectorMapper {

    UserActionAvro mapToAvro(UserActionProto action);

    default ActionTypeAvro mapActionType(ActionTypeProto actionType) {
        if (actionType == null) {
            return null;
        }
        return switch (actionType) {
            case ACTION_VIEW -> ActionTypeAvro.VIEW;
            case ACTION_REGISTER -> ActionTypeAvro.REGISTER;
            case ACTION_LIKE -> ActionTypeAvro.LIKE;
            default -> throw new IllegalArgumentException("Неизвестный тип действия: " + actionType);
        };
    }

    default Instant mapTimestamp(Timestamp timestamp) {
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    }
}