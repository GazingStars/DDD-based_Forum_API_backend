package com.example.forum.web.Controllers;

import com.example.forum.application.DTOs.user.RegisterRequest;
import com.example.forum.application.services.AuthService;
import com.example.forum.application.services.UserService;
import com.example.forum.domain.model.user.User;
import com.example.forum.infrastructure.security.JwtProvider;
import com.example.forum.web.DTOs.auth.LoginRequest;
import com.example.forum.web.DTOs.auth.LoginResponse;
import com.example.forum.web.DTOs.user.UserResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final JwtProvider jwtProvider;

    public AuthController(UserService userService, AuthService authService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.authService = authService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody RegisterRequest request) {
        User user = userService.register(request);
        return UserResponse.from(user);
    }

    @PostMapping(
            value = "/login",
            consumes = "application/json",
            produces = "application/json"
    )
    public LoginResponse login(@RequestBody LoginRequest request) {

        User user = authService.login(request.email(), request.password());

        String token = jwtProvider.generateToken(user);

        return new LoginResponse(token);
    }
}
