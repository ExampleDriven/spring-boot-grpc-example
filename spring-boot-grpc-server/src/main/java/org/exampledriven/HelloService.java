package org.exampledriven;

import io.grpc.stub.StreamObserver;
import org.exampledriven.grpc.services.HelloRequest;
import org.exampledriven.grpc.services.HelloResponse;
import org.exampledriven.grpc.services.HelloServiceGrpc;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GRpcService
public class HelloService extends HelloServiceGrpc.HelloServiceImplBase {

    private Logger logger = LoggerFactory.getLogger(HelloService.class);

    @Override
    public void greet(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {

        logger.debug("Request " + request);

        HelloResponse helloResponse = HelloResponse.newBuilder().
                setGreeting("Hi " + request.getName())
                .build();

        logger.debug("Response " + helloResponse);

        responseObserver.onNext(helloResponse);
        responseObserver.onCompleted();

    }
}
