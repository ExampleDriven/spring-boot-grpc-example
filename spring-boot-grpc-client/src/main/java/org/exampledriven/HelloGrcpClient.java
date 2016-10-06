package org.exampledriven;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.util.RoundRobinLoadBalancerFactory;
import org.exampledriven.grpc.services.HelloRequest;
import org.exampledriven.grpc.services.HelloResponse;
import org.exampledriven.grpc.services.HelloServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HelloGrcpClient {

    private Logger logger = LoggerFactory.getLogger(HelloGrcpClient.class);

    public HelloResponse greet(String name) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565)
            .loadBalancerFactory(RoundRobinLoadBalancerFactory.getInstance())
            .usePlaintext(true)
            .build();

        HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub = HelloServiceGrpc.newBlockingStub(channel);

        HelloRequest helloRequest = HelloRequest.newBuilder().setName(name).build();

        logger.debug("Request " + helloRequest);

        HelloResponse helloResponse = helloServiceBlockingStub.greet(helloRequest);

        logger.debug("Response " + helloResponse);

        return helloResponse;

    }

}
