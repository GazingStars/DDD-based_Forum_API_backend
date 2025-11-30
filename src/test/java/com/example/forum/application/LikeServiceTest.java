package com.example.forum.application;

import com.example.forum.application.services.LikeService;
import com.example.forum.application.services.PostService;
import com.example.forum.application.services.UserService;
import com.example.forum.domain.model.like.LikeTargetType;
import com.example.forum.domain.model.post.Post;
import com.example.forum.domain.model.post.PostId;
import com.example.forum.domain.model.user.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class LikeServiceTest {

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
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
    }

    @Autowired LikeService likeService;
    @Autowired PostService postService;
    @Autowired UserService userService;
    @Autowired JdbcTemplate jdbc;

    User user;

    final String CATEGORY_ID = "cat-1";

    @BeforeEach
    void setup() {
        jdbc.execute("TRUNCATE TABLE likes, posts, users, categories CASCADE");

        jdbc.update("""
            INSERT INTO categories(id, name, slug, description)
            VALUES (?, ?, ?, ?)
        """, CATEGORY_ID, "General", "general", null);

        user = userService.register(
                new com.example.forum.application.DTOs.user.RegisterRequest(
                        "like@test.com", "liker", "12345678")
        );
    }

    // ---------------------------------------------------------
    @Test
    void userCanLikePost() {

        Post post = postService.createPost(
                user.getId(),
                new com.example.forum.application.DTOs.post.CreatePostRequest(
                        "Hello","World", CATEGORY_ID
                )
        );

        PostId postId = post.getId();

        // before: no likes
        Assertions.assertEquals(0, likeService.countPostLikes(postId));

        // like
        likeService.likePost(user.getId(), postId);

        // now: 1 like
        Assertions.assertEquals(1, likeService.countPostLikes(postId));

        // second like should NOT increase count
        likeService.likePost(user.getId(), postId);
        Assertions.assertEquals(1, likeService.countPostLikes(postId));
    }

    // ---------------------------------------------------------
    @Test
    void userCanUnlikePost() {

        Post post = postService.createPost(
                user.getId(),
                new com.example.forum.application.DTOs.post.CreatePostRequest(
                        "Hello","World", CATEGORY_ID
                )
        );

        PostId postId = post.getId();

        likeService.likePost(user.getId(), postId);
        Assertions.assertEquals(1, likeService.countPostLikes(postId));

        likeService.unlikePost(user.getId(), postId);
        Assertions.assertEquals(0, likeService.countPostLikes(postId));

        // second unlike should not throw
        likeService.unlikePost(user.getId(), postId);
        Assertions.assertEquals(0, likeService.countPostLikes(postId));
    }

    // ---------------------------------------------------------
    @Test
    void existsShouldWorkCorrectly() {

        Post post = postService.createPost(
                user.getId(),
                new com.example.forum.application.DTOs.post.CreatePostRequest(
                        "Test","TestC", CATEGORY_ID
                )
        );

        PostId postId = post.getId();

        Assertions.assertFalse(likeService.hasUserLikedPost(user.getId(), postId));

        likeService.likePost(user.getId(), postId);

        Assertions.assertTrue(likeService.hasUserLikedPost(user.getId(), postId));
    }


}
