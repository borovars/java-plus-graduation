package ru.practicum.analyzer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.analyzer.model.Similarity;
import ru.yandex.practicum.ewm.stats.avro.EventSimilarityAvro;

@Mapper(componentModel = "spring")
public interface SimilarityMapper {

    @Mapping(target = "id", ignore = true)
    Similarity toSimilarity(EventSimilarityAvro avro);
}