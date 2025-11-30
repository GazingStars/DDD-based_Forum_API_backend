package com.example.forum.application.services;

import com.example.forum.domain.common.PasswordHasher;
import com.example.forum.domain.model.user.Email;
import com.example.forum.domain.model.user.User;
import com.example.forum.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public AuthService(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    @Transactional
    public User login(String email, String rawPassword) {

        Email emailVo = new Email(email);

        User user = userRepository.findByEmail(emailVo)
                .orElseThrow(() -> new IllegalStateException("Invalid credentials"));

        if (!passwordHasher.verify(rawPassword, user.getPasswordHash())) {
            throw new IllegalStateException("Invalid credentials");
        }

        return user;
    }
}
