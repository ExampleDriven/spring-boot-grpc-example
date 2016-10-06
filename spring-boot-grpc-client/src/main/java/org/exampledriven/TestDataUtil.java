package org.exampledriven;

import org.exampledriven.grpc.services.Book;
import org.exampledriven.grpc.services.BookType;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Peter_Szanto on 10/6/2016.
 */
public class TestDataUtil {

    private static List<Book> grpcBookList = new LinkedList<>();
    private static List<org.exampledriven.rest.Book> restBookList = new LinkedList<>();

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

        for (int i = 0; i < 10; i++) {
            org.exampledriven.rest.Book book = new org.exampledriven.rest.Book();
            book.setAuthor(randomString());
            book.setBookType(org.exampledriven.rest.Book.BookType.COMIC_BOOK);

            book.setKeyword(new LinkedList<>());
            for (int j = 0; j < 3; j++) {
                book.getKeyword().add(randomString());
            }

            book.setPage((int)(Math.random() * 100));
            book.setTitle(randomString());

            restBookList.add(book);

        }


    }

    private static String randomString() {
        return UUID.randomUUID().toString();
    }

    public static List<Book> getGrpcTestData() {
        return grpcBookList;
    }

    public static List<org.exampledriven.rest.Book> getRestTestData() {
        return restBookList;
    }
}
