package ru.practicum.collector;

import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.practicum.ewm.stats.service.collector.UserActionControllerGrpc;

@GrpcService
@RequiredArgsConstructor
public class CollectorController extends UserActionControllerGrpc.UserActionControllerImplBase {

    private final KafkaProducer producer;
}
