package com.example.forum.application;

import com.example.forum.application.DTOs.user.RegisterRequest;
import com.example.forum.application.DTOs.user.UpdateUserRequest;
import com.example.forum.application.services.UserService;
import com.example.forum.domain.model.user.Role;
import com.example.forum.domain.model.user.User;
import com.example.forum.domain.model.user.UserId;
import com.example.forum.domain.repository.UserRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
public class UserServiceTest {

    static {
        System.setProperty("spring.flyway.enabled", "true");
        System.setProperty("spring.flyway.locations", "classpath:db/migration");
    }

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
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    JdbcTemplate jdbc;

    @BeforeEach
    void cleanDb() {
        jdbc.execute("TRUNCATE TABLE posts, users CASCADE");
    }

    // -------------------------------------------------------------
    @Test
    void shouldRegisterUser() {

        RegisterRequest req = new RegisterRequest(
                "test@mail.com",
                "john",
                "123456"
        );

        User user = userService.register(req);

        Assertions.assertNotNull(user);
        Assertions.assertEquals("test@mail.com", user.getEmail().get());
        Assertions.assertEquals("john", user.getUsername().get());

        Assertions.assertNull(user.getAvatarId());
    }

    // -------------------------------------------------------------
    @Test
    void shouldRejectDuplicateEmail() {

        RegisterRequest req = new RegisterRequest(
                "test@mail.com",
                "john",
                "123456"
        );

        userService.register(req);

        assertThrows(IllegalStateException.class, () ->
                userService.register(req)
        );
    }

    // -------------------------------------------------------------
    @Test
    void shouldGetUserById() {

        User user = userService.register(
                new RegisterRequest("a@mail.com", "aaa", "pass")
        );

        User loaded = userService.getById(user.getId());

        Assertions.assertEquals(user.getId().get(), loaded.getId().get());
        Assertions.assertEquals("aaa", loaded.getUsername().get());
    }

    @Test
    void shouldThrowIfUserNotFound() {
        assertThrows(RuntimeException.class, () ->
                userService.getById(new UserId("non-existing-id"))
        );
    }

    // -------------------------------------------------------------
    @Test
    void shouldUpdateUser() {

        User user = userService.register(
                new RegisterRequest("a@mail.com", "aaa", "123456")
        );

        UpdateUserRequest upd = new UpdateUserRequest(
                "a@mail.com",
                "newname",
                null
        );

        User u = userService.updateUser(user.getId(), upd);

        Assertions.assertEquals("newname", u.getUsername().get());
    }

    // -------------------------------------------------------------
    @Test
    void shouldBanUser() {

        User user = userService.register(
                new RegisterRequest("a@mail.com", "aaa", "123456")
        );

        userService.banUser(user.getId(), Role.ADMIN);

        User banned = userRepository.findById(user.getId()).orElseThrow();

        Assertions.assertTrue(banned.isBlocked());
    }

    @Test
    void userCannotBanUser() {

        User user = userService.register(
                new RegisterRequest("a@mail.com", "aaa", "123456")
        );

        assertThrows(RuntimeException.class, () ->
                userService.banUser(user.getId(), Role.USER)
        );
    }

    // -------------------------------------------------------------
    @Test
    void adminCanChangeRole() {

        User user = userService.register(
                new RegisterRequest("a@mail.com", "aaa", "123456")
        );

        userService.changeRole(user.getId(), Role.MODERATOR, Role.ADMIN);

        User changed = userRepository.findById(user.getId()).orElseThrow();

        Assertions.assertEquals(Role.MODERATOR, changed.getRole());
    }

    @Test
    void userCannotChangeRole() {

        User user = userService.register(
                new RegisterRequest("a@mail.com", "aaa", "123456")
        );

        assertThrows(RuntimeException.class, () ->
                userService.changeRole(user.getId(), Role.MODERATOR, Role.USER)
        );
    }
}
