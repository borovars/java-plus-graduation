package ru.practicum.stats;

import com.google.protobuf.Timestamp;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.proto.ActionTypeProto;
import ru.practicum.ewm.stats.proto.UserActionProto;
import ru.practicum.ewm.stats.service.collector.UserActionControllerGrpc;

import java.time.Instant;

@Component
public class CollectorClient {

    @GrpcClient("collector")
    UserActionControllerGrpc.UserActionControllerBlockingStub server;

    public void collectUserAction(Long userId, Long eventId, String actionType, Instant timestamp) {
        UserActionProto request = UserActionProto.newBuilder()
                .setUserId(userId)
                .setEventId(eventId)
                .setType(ActionTypeProto.valueOf(actionType))
                .setTimestamp(buildTimestamp(timestamp))
                .build();
        server.collectUserAction(request);
    }

    private Timestamp buildTimestamp(Instant instant) {
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }
}