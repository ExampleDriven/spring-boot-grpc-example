package org.exampledriven.rest;

import org.exampledriven.BookUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class BooksRestController {
    @Autowired
    HttpMessageConverters httpMessageConverters;

    @PostMapping("book")
    public List<Book> createBooks(@RequestBody List<Book> books) {

        books.forEach(book -> {
            book.setISBN(BookUtil.generateISBN());
        });

        return books;
    }

    @GetMapping()
    public void test() {
        httpMessageConverters.getConverters().forEach(System.out::println);
    }

}
