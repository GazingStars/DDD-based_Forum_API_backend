package com.example.forum;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/protected")
    public String protectedEndpoint() {
        return "OK";
    }
}
