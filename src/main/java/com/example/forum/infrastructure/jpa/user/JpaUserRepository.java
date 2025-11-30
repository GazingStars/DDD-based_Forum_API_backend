package com.example.forum.infrastructure.jpa.user;

import com.example.forum.domain.model.user.Email;
import com.example.forum.domain.model.user.User;
import com.example.forum.domain.model.user.UserId;
import com.example.forum.domain.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaUserRepository implements UserRepository {

    SpringDataUserRepo repo;

    public JpaUserRepository(SpringDataUserRepo repo) {
        this.repo = repo;
    }

    @Override
    public User save(User user) {
        UserEntity saved = repo.save(UserMapper.toEntity(user));
        return UserMapper.toDomain(saved);
    }

    @Override
    public List<User> search(String query) {
        return repo.search(query)
                .stream()
                .map(UserMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return repo.findByEmail(email.get()).map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findById(UserId userId) {
        return repo.findById(userId.get()).map(UserMapper::toDomain);
    }
}
