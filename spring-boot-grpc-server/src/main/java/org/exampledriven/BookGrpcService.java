package org.exampledriven;

import io.grpc.stub.StreamObserver;
import org.exampledriven.grpc.services.BookList;
import org.exampledriven.grpc.services.BookServiceGrpc;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

@GRpcService
public class BookGrpcService extends BookServiceGrpc.BookServiceImplBase {

    private Logger logger = LoggerFactory.getLogger(BookGrpcService.class);

    @Value("${grpc.port}")
    private String grpcPort;

    @Override
    public void createBooks(BookList request, StreamObserver<BookList> responseObserver) {
        logger.debug("Request " + request);

        BookList.Builder responseBuilder = BookList.newBuilder();

        BookUtil.assignISBN(request.getBookList()).forEach(responseBuilder::addBook);

        BookList bookListResponse = responseBuilder
                .setServerIdentifier(String.format("server-%s", grpcPort))
                .build();

        logger.debug("Response " + bookListResponse);

        responseObserver.onNext(bookListResponse);
        responseObserver.onCompleted();
    }

}
