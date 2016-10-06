package org.exampledriven.rest;

import org.exampledriven.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;

@RestController
public class BookRestController {

    @RequestMapping("/test_rest")
    public List<Book> test(@RequestParam(value = "compact", required = false, defaultValue = "false") boolean compact) {

        HttpEntity<List<Book>> entity = new HttpEntity<List<Book>>(org.exampledriven.TestDataUtil.getRestTestData(), null);

        ResponseEntity<List<Book>> responseEntity =
            new RestTemplate().exchange("http://localhost:8081/book",
                    HttpMethod.POST, entity, new ParameterizedTypeReference<List<Book>>() {});

        if (compact) {
            return null;
        } else {
            return responseEntity.getBody();
        }
    }

}
