package org.exampledriven.rest;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@RestController
public class BookRestController {

    @RequestMapping("/test_rest")
    public List<Book> testVerbose() {
        HttpEntity<List<Book>> entity = new HttpEntity<>(TestDataUtil.getRestTestData(), null);

        ResponseEntity<List<Book>> responseEntity =
            new RestTemplate().exchange("http://localhost:8081/book",
                    HttpMethod.POST, entity, new ParameterizedTypeReference<List<Book>>() {});

        return responseEntity.getBody();
    }


    @RequestMapping("/test_rest/compact")
    public String testCompact() {

        testVerbose();

        return "SUCCESS";
    }


}
