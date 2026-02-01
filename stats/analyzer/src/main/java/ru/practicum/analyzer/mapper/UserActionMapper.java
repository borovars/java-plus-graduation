package ru.practicum.analyzer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.analyzer.model.ActionType;
import ru.practicum.analyzer.model.UserAction;
import ru.yandex.practicum.ewm.stats.avro.ActionTypeAvro;
import ru.yandex.practicum.ewm.stats.avro.UserActionAvro;

@Mapper(componentModel = "spring")
public interface UserActionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "actionWeight", expression = "java(calculateActionWeight(request.getActionType()))")
    UserAction toEntity(UserActionAvro request);

    default ActionType mapActionType(ActionTypeAvro actionType) {
        if (actionType == null) {
            return null;
        }
        return switch (actionType) {
            case VIEW -> ActionType.VIEW;
            case REGISTER -> ActionType.REGISTER;
            case LIKE -> ActionType.LIKE;
            default -> throw new IllegalArgumentException("Неизвестный тип действия: " + actionType);
        };
    }

    default Double calculateActionWeight(ActionTypeAvro actionType) {
        return switch (actionType) {
            case VIEW -> 0.4;
            case REGISTER -> 0.8;
            case LIKE -> 1.0;
            default -> throw new IllegalArgumentException("Неизвестный тип действия: " + actionType);
        };
    }
}