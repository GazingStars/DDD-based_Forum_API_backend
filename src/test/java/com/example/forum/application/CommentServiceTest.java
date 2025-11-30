package com.example.forum.application;

import com.example.forum.application.DTOs.comment.CreateCommentRequest;
import com.example.forum.application.DTOs.comment.UpdateCommentRequest;
import com.example.forum.application.DTOs.user.RegisterRequest;
import com.example.forum.application.services.CommentService;
import com.example.forum.application.services.PostService;
import com.example.forum.application.services.UserService;
import com.example.forum.domain.model.comment.Comment;
import com.example.forum.domain.model.post.Post;
import com.example.forum.domain.model.user.Role;
import com.example.forum.domain.model.user.User;

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

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
public class CommentServiceTest {

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
    CommentService commentService;

    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @Autowired
    JdbcTemplate jdbc;

    User author;
    User moderator;
    User stranger;
    Post post;

    final String CATEGORY_ID = "cat-1";

    @BeforeEach
    void setup() {

        jdbc.update("TRUNCATE TABLE comments, posts, users, categories CASCADE");

        jdbc.update("""
                INSERT INTO categories(id, name, slug)
                VALUES (?, ?, 'general')
                """,
                CATEGORY_ID, "General"
        );

        author = userService.register(
                new RegisterRequest("a@test.com", "author", "12345678")
        );

        moderator = userService.register(
                new RegisterRequest("m@test.com", "moderator", "12345678")
        );

        jdbc.update("UPDATE users SET role='MODERATOR' WHERE id=?", moderator.getId().get());

        stranger = userService.register(
                new RegisterRequest("x@test.com", "stranger", "12345678")
        );

        post = postService.createPost(
                author.getId(),
                new com.example.forum.application.DTOs.post.CreatePostRequest(
                        "PostTitle", "PostContent", CATEGORY_ID
                )
        );
    }

    // ------------------------ CREATE COMMENT ------------------------
    @Test
    void shouldCreateComment() {

        CreateCommentRequest req = new CreateCommentRequest("Hello!");

        Comment comment = commentService.createComment(
                post.getId(),
                author.getId(),
                req
        );

        Assertions.assertNotNull(comment.id());
        Assertions.assertEquals("Hello!", comment.content().value());

        int count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM comments WHERE id = ?",
                Integer.class,
                comment.id().value()
        );

        Assertions.assertEquals(1, count);
    }

    // ------------------------ EDIT COMMENT ---------------------------
    @Test
    void authorShouldEditOwnComment() {

        Comment comment = commentService.createComment(
                post.getId(),
                author.getId(),
                new CreateCommentRequest("Old")
        );

        UpdateCommentRequest upd = new UpdateCommentRequest("New");

        Comment updated = commentService.editComment(
                comment.id(),
                author.getId(),
                upd
        );

        Assertions.assertEquals("New", updated.content().value());
    }

    @Test
    void shouldForbidEditingForeignComment() {

        Comment comment = commentService.createComment(
                post.getId(),
                author.getId(),
                new CreateCommentRequest("Old")
        );

        UpdateCommentRequest upd = new UpdateCommentRequest("Hack");

        Assertions.assertThrows(RuntimeException.class, () ->
                commentService.editComment(
                        comment.id(),
                        stranger.getId(),
                        upd
                )
        );
    }

    // ------------------------ DELETE COMMENT --------------------------
    @Test
    void authorCanDeleteOwnComment() {

        Comment comment = commentService.createComment(
                post.getId(),
                author.getId(),
                new CreateCommentRequest("AAA")
        );

        commentService.deleteComment(
                comment.id(),
                author.getId(),
                Role.USER
        );

        int count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM comments WHERE id = ?",
                Integer.class,
                comment.id().value()
        );

        Assertions.assertEquals(0, count);
    }

    @Test
    void moderatorCanDeleteForeignComment() {

        Comment comment = commentService.createComment(
                post.getId(),
                author.getId(),
                new CreateCommentRequest("AAA")
        );

        commentService.deleteComment(
                comment.id(),
                moderator.getId(),
                Role.MODERATOR
        );

        int count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM comments WHERE id = ?",
                Integer.class,
                comment.id().value()
        );

        Assertions.assertEquals(0, count);
    }

    @Test
    void shouldForbidDeletingForeignComment() {

        Comment comment = commentService.createComment(
                post.getId(),
                author.getId(),
                new CreateCommentRequest("AAA")
        );

        Assertions.assertThrows(RuntimeException.class, () ->
                commentService.deleteComment(
                        comment.id(),
                        stranger.getId(),
                        Role.USER
                )
        );
    }

    // ------------------------ PAGINATION ------------------------------
    @Test
    void shouldReturnPagedComments() {

        for (int i = 0; i < 5; i++) {
            commentService.createComment(
                    post.getId(),
                    author.getId(),
                    new CreateCommentRequest("C" + i)
            );
        }

        var list = commentService.getCommentsPaged(post.getId(), 0, 3);

        Assertions.assertEquals(3, list.size());
    }
}
