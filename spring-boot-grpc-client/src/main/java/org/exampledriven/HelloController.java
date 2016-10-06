package org.exampledriven;

import org.exampledriven.grpc.services.HelloResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    HelloGrcpClient helloGrcpClient;

    @RequestMapping("/greet")
    public String greet(@RequestParam("name") String name) {
        HelloResponse greet = helloGrcpClient.greet(name);
        return greet.getGreeting();
    }

}
