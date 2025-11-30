package com.example.forum.infrastructure;

import com.example.forum.domain.model.user.*;
import com.example.forum.infrastructure.jpa.user.JpaUserRepository;
import com.example.forum.infrastructure.jpa.user.SpringDataUserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
public class JpaUserRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("testdb")
                    .withUsername("postgres")
                    .withPassword("postgres");

    @DynamicPropertySource
    static void registerPgProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
    }

    @Autowired
    private SpringDataUserRepo springRepo;

    private JpaUserRepository repo;

    @BeforeEach
    void setup() {
        repo = new JpaUserRepository(springRepo);
        springRepo.deleteAll(); // очистка таблицы
    }

    @Test
    void shouldSaveAndLoadUser() {

        User user = new User(
                UserId.generate(),
                new Email("test@mail.com"),
                new Username("john"),
                "long-hashed-password-value",
                Role.USER,
                null,
                false,
                false,
                Instant.now(),
                Instant.now(),
                null
        );

        User saved = repo.save(user);

        Optional<User> loaded = repo.findByEmail(new Email("test@mail.com"));

        assertTrue(loaded.isPresent());
        assertEquals(saved.getId().get(), loaded.get().getId().get());
        assertEquals("john", loaded.get().getUsername().get());
    }
}
