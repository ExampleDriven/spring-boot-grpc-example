package org.exampledriven;

import io.grpc.stub.StreamObserver;
import org.exampledriven.grpc.services.Book;
import org.exampledriven.grpc.services.BookList;
import org.exampledriven.grpc.services.BookServiceGrpc;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@GRpcService
public class BookGrpcService extends BookServiceGrpc.BookServiceImplBase {

    private Logger logger = LoggerFactory.getLogger(BookGrpcService.class);

    @Override
    public void createBooks(BookList request, StreamObserver<BookList> responseObserver) {
        logger.debug("Request " + request);

        BookList.Builder responseBuilder = BookList.newBuilder();

        BookUtil.assignISBN(request.getBookList()).forEach(responseBuilder::addBook);

        BookList bookListResponse = responseBuilder.build();

        logger.debug("Response " + bookListResponse);

        responseObserver.onNext(bookListResponse);
        responseObserver.onCompleted();
    }

}
