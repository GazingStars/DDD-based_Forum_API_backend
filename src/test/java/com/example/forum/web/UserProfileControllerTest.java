package com.example.forum.web;

import com.example.forum.application.DTOs.user.UpdateUserRequest;
import com.example.forum.application.services.AuthService;
import com.example.forum.application.services.UserService;
import com.example.forum.domain.model.user.*;
import com.example.forum.domain.repository.UserRepository;
import com.example.forum.infrastructure.security.AuthUserPrincipal;
import com.example.forum.infrastructure.security.JwtProvider;
import com.example.forum.web.Controllers.UserProfileController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserProfileController.class)
@AutoConfigureMockMvc
class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    void updateMe_shouldUpdateProfile() throws Exception {

        String userId = "123";
        AuthUserPrincipal principal = new AuthUserPrincipal(userId, Role.USER);

        UpdateUserRequest request =
                new UpdateUserRequest(
                        "new@mail.com",
                        "newname",
                        null
                );

        User updatedUser = new User(
                new UserId(userId),
                new Email("new@mail.com"),
                new Username("newname"),
                "hashed-pass",
                Role.USER,
                null,
                false,
                false,
                Instant.now(),
                Instant.now(),
                null
        );

        when(userService.updateUser(any(UserId.class), any(UpdateUserRequest.class)))
                .thenReturn(updatedUser);

        mockMvc.perform(
                        put("/api/user/me")
                                .with(csrf())
                                .with(authentication(auth(principal)))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "email": "new@mail.com",
                                          "username": "newname"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("new@mail.com"))
                .andExpect(jsonPath("$.username").value("newname"));
    }

    private Authentication auth(AuthUserPrincipal principal) {
        return new UsernamePasswordAuthenticationToken(
                principal,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
