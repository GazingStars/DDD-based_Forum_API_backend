package com.example.forum.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestSecurityController {

    @GetMapping("/api/admin/secured")
    public String adminEndpoint() {
        return "ADMIN_OK";
    }

    @GetMapping("/api/mod/secured")
    public String modEndpoint() {
        return "MOD_OK";
    }

    @GetMapping("/api/open")
    public String openEndpoint() {
        return "OPEN_OK";
    }
}
