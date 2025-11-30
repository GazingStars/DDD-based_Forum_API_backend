package com.example.forum.application.services;

import com.example.forum.application.DTOs.user.RegisterRequest;
import com.example.forum.application.DTOs.user.UpdateUserRequest;
import com.example.forum.domain.common.PasswordHasher;
import com.example.forum.domain.model.user.*;
import com.example.forum.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public UserService(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    @Transactional
    public User register(RegisterRequest request) {

        Email email = new Email(request.email());

        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("Email already taken");
        }

        String hashed = passwordHasher.hash(request.password());

        User user = User.createNew(
                email,
                new Username(request.username()),
                hashed,
                null
        );

        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(UserId id, UpdateUserRequest req) {

        User user = userRepository.findById(id)
                .orElseThrow();

        if (req.email() != null && !req.email().isBlank()
            && !user.getEmail().get().equals(req.email())) {

            Email newEmail = new Email(req.email());

            if (userRepository.findByEmail(newEmail).isPresent())
                throw new IllegalStateException("Email taken");

            user.changeEmail(newEmail);
        }

        if (req.username() != null && !req.username().isBlank()
            && !user.getUsername().get().equals(req.username())) {

            user.changeUsername(new Username(req.username()));
        }

        if (req.newPassword() != null && !req.newPassword().isBlank()) {
            user.changePasswordHash(passwordHasher.hash(req.newPassword()));
        }

        return userRepository.save(user);
    }

    @Transactional
    public User banUser(UserId id, Role role) {
        if (role != Role.ADMIN)
            throw new RuntimeException("Forbidden");

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.block();

        return userRepository.save(user);
    }

    @Transactional
    public User changeRole(UserId id, Role newRole, Role actorRole) {
        if (actorRole != Role.ADMIN)
            throw new RuntimeException("Forbidden");

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.changeRole(newRole);

        return userRepository.save(user);
    }

    public List<User> searchUsers(String query) {
        if (query == null || query.isBlank()) return List.of();
        return userRepository.search(query);
    }

    @Transactional
    public User getById(UserId id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }
}