package org.exampledriven.grpc;

import org.exampledriven.grpc.services.Book;
import org.exampledriven.grpc.services.BookList;
import org.exampledriven.grpc.services.BookServiceGrpc.BookServiceBlockingStub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceGrpcClient {

    private final Logger logger = LoggerFactory.getLogger(BookServiceGrpcClient.class);

    private final BookServiceBlockingStub bookService;

    public BookServiceGrpcClient(BookServiceBlockingStub bookService) {
        this.bookService = bookService;
    }

    public BookList createBooks(List<Book> bookList) {

        BookList.Builder builder = BookList.newBuilder();
        bookList.forEach(builder::addBook);
        BookList request = builder.build();

        if (logger.isDebugEnabled()) {
            logger.debug("Request " + request);
        }
        BookList response = bookService.createBooks(request);
        if (logger.isDebugEnabled()) {
            logger.debug("Response " + response);
        }
        return response;
    }

}
