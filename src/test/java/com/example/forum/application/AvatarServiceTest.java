package com.example.forum.application;

import com.example.forum.application.services.AvatarService;
import com.example.forum.application.DTOs.user.RegisterRequest;

import com.example.forum.application.storage.FakeFileStorage;
import com.example.forum.domain.model.avatar.Avatar;
import com.example.forum.domain.model.user.User;

import com.example.forum.application.services.UserService;
import com.example.forum.domain.repository.AvatarRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.mock.web.MockMultipartFile;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
public class AvatarServiceTest {

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
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
    }

    @Autowired
    AvatarService avatarService;

    @Autowired
    UserService userService;

    @Autowired
    AvatarRepository avatarRepository;

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    FakeFileStorage fileStorage;

    User user;

    @BeforeEach
    void setup() {
        jdbc.update("TRUNCATE TABLE avatars, users CASCADE");

        user = userService.register(
                new RegisterRequest("test@mail.com", "tester", "12345678")
        );
    }

    /** ------- UPLOAD AVATAR -------- */
    @Test
    void shouldUploadAvatar() throws IOException {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "avatar.png",
                "image/png",
                new byte[]{1,2,3}
        );

        Avatar avatar = avatarService.upload(user.getId(), file);

        Assertions.assertNotNull(avatar.getId());
        Assertions.assertTrue(avatar.getFileName().endsWith("avatar.png"));

        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM avatars WHERE id = ?",
                Integer.class,
                avatar.getId().value()
        );

        Assertions.assertEquals(1, count);

        String avatarIdInDb = jdbc.queryForObject(
                "SELECT avatar_id FROM users WHERE id = ?",
                String.class,
                user.getId().get()
        );

        Assertions.assertEquals(avatar.getId().value(), avatarIdInDb);
    }


    /** ------- REPLACE AVATAR -------- */
    @Test
    void shouldReplaceOldAvatar() throws IOException {

        // upload #1
        MockMultipartFile file1 = new MockMultipartFile(
                "file",
                "old.png",
                "image/png",
                new byte[]{1,2,3}
        );

        Avatar a1 = avatarService.upload(user.getId(), file1);

        // upload #2
        MockMultipartFile file2 = new MockMultipartFile(
                "file",
                "new.png",
                "image/png",
                new byte[]{4,5,6}
        );

        Avatar a2 = avatarService.upload(user.getId(), file2);

        Integer oldCount = jdbc.queryForObject(
                "SELECT COUNT(*) FROM avatars WHERE id = ?",
                Integer.class,
                a1.getId().value()
        );
        Assertions.assertEquals(0, oldCount);

        Integer newCount = jdbc.queryForObject(
                "SELECT COUNT(*) FROM avatars WHERE id = ?",
                Integer.class,
                a2.getId().value()
        );
        Assertions.assertEquals(1, newCount);

        String avatarIdInDb = jdbc.queryForObject(
                "SELECT avatar_id FROM users WHERE id = ?",
                String.class,
                user.getId().get()
        );

        Assertions.assertEquals(a2.getId().value(), avatarIdInDb);
    }


    /** ------- DELETE AVATAR -------- */
    @Test
    void shouldDeleteAvatar() throws IOException {

        // upload
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "avatar.png",
                "image/png",
                new byte[]{1,2,3}
        );

        Avatar a = avatarService.upload(user.getId(), file);

        avatarService.delete(user.getId());

        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM avatars WHERE id = ?",
                Integer.class,
                a.getId().value()
        );

        Assertions.assertEquals(0, count);
    }
}
