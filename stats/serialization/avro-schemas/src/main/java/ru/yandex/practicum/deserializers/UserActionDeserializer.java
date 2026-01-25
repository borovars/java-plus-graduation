package ru.yandex.practicum.deserializers;

import ru.yandex.practicum.ewm.stats.avro.UserActionAvro;

public class UserActionDeserializer extends BaseAvroDeserializer<UserActionAvro> {
    public UserActionDeserializer() {
        super(UserActionAvro.getClassSchema());
    }
}
