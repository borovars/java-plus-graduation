package ru.yandex.practicum.deserializers;

import ru.yandex.practicum.ewm.stats.avro.EventSimilarityAvro;

public class SimilarityDeserializer extends BaseAvroDeserializer<EventSimilarityAvro> {
    public SimilarityDeserializer() {
        super(EventSimilarityAvro.getClassSchema());
    }
}