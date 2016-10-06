package org.exampledriven;

import org.exampledriven.grpc.services.Book;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


public class BookUtil {

    public static List<Book> assignISBN(List<Book> books) {

        List<Book> result = new LinkedList<>();
        for (Book book : books) {
            Book bookWithISBN = Book.newBuilder(book)
                    .setISBN(generateISBN())
                    .build();

            result.add(bookWithISBN);
        }

        return result;
    }

    public static String generateISBN() {
        return UUID.randomUUID().toString().replaceAll("/", "").substring(0,12);
    }
}
