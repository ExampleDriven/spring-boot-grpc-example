package org.exampledriven;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.util.RoundRobinLoadBalancerFactory;
import org.exampledriven.grpc.services.Book;
import org.exampledriven.grpc.services.BookList;
import org.exampledriven.grpc.services.BookServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceGrpcClient {

    private Logger logger = LoggerFactory.getLogger(BookServiceGrpcClient.class);

    public BookList createBooks(List<Book> bookList) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .loadBalancerFactory(RoundRobinLoadBalancerFactory.getInstance())
                .usePlaintext(true)
                .build();

        BookServiceGrpc.BookServiceBlockingStub bookServiceBlockingStub = BookServiceGrpc.newBlockingStub(channel);

        BookList.Builder builder = BookList.newBuilder();
        bookList.forEach(builder::addBook);
        BookList request = builder.build();

        logger.debug("Request " + request);
        BookList response = bookServiceBlockingStub.createBooks(request);
        logger.debug("Response " + response);

        return response;

    }

}
