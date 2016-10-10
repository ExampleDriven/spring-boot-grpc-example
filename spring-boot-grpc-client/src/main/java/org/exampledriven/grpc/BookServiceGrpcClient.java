package org.exampledriven.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.util.RoundRobinLoadBalancerFactory;
import org.exampledriven.grpc.services.Book;
import org.exampledriven.grpc.services.BookList;
import org.exampledriven.grpc.services.BookServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class BookServiceGrpcClient {

    private Logger logger = LoggerFactory.getLogger(BookServiceGrpcClient.class);

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    private BookServiceGrpc.BookServiceBlockingStub bookServiceBlockingStub;

    public BookList createBooks(List<Book> bookList) {

        BookList.Builder builder = BookList.newBuilder();
        bookList.forEach(builder::addBook);
        BookList request = builder.build();

        logger.debug("Request " + request);
        BookList response = bookServiceBlockingStub.createBooks(request);
        logger.debug("Response " + response);

        return response;

    }

    @PostConstruct
    private void initializeClient() {
        ServiceInstance serviceInstance = loadBalancerClient.choose("grpc-server");
        String host = serviceInstance.getHost();

        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, 6565)
//                .nameResolverFactory()
                .loadBalancerFactory(RoundRobinLoadBalancerFactory.getInstance())
                .usePlaintext(true)
                .build();

        bookServiceBlockingStub = BookServiceGrpc.newBlockingStub(channel);
    }

}
