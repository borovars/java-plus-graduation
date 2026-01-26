package ru.practicum.collector;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.practicum.ewm.stats.proto.UserActionProto;
import ru.practicum.ewm.stats.service.collector.UserActionControllerGrpc;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class CollectorController extends UserActionControllerGrpc.UserActionControllerImplBase {

    private final CollectorService service;

    public void collectUserAction(UserActionProto request, StreamObserver<Empty> response) {
        log.info("Получение действий из grpc");
        try {
                service.createUserAction(request);
                response.onNext(Empty.getDefaultInstance());
                response.onCompleted();
        } catch (Exception e){
            response.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }
}
