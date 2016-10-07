package org.exampledriven.grpc;

import org.exampledriven.grpc.services.Book;
import org.exampledriven.grpc.services.BookType;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class TestDataUtil {

    private static List<Book> grpcBookList = new LinkedList<>();

    static {

        for (int i = 0; i < 10; i++) {
            Book.Builder builder = Book.newBuilder()
                .setAuthor(randomString())
                .setBookType(BookType.COMIC_BOOK);

            for (int j = 0; j < 3; j++) {
                builder.addKeyword(randomString());
            }

            builder
                .setPage((int)(Math.random() * 100))
                .setTitle(randomString());

            grpcBookList.add(builder.build());

        }

    }

    private static String randomString() {
        return UUID.randomUUID().toString();
    }

    public static List<Book> getGrpcTestData() {
        return grpcBookList;
    }

}
