package com.example.forum.domain.repository;

import com.example.forum.domain.model.user.Email;
import com.example.forum.domain.model.user.User;
import com.example.forum.domain.model.user.UserId;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findByEmail(Email email);

    List<User> search(String query);

    Optional<User> findById(UserId userId);
}
