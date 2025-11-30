package com.example.forum.application;

import com.example.forum.application.DTOs.post.CreatePostRequest;
import com.example.forum.application.DTOs.post.UpdatePostRequest;
import com.example.forum.application.DTOs.user.RegisterRequest;
import com.example.forum.application.services.PostService;
import com.example.forum.application.services.UserService;
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
public class PostServiceTest {

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
    PostService postService;

    @Autowired
    UserService userService;

    @Autowired
    JdbcTemplate jdbc;

    User user;
    User moderator;
    User stranger;

    final String CATEGORY_ID = "cat-1";

    @BeforeEach
    void setupUsers() {

        jdbc.update("TRUNCATE TABLE posts, users, categories CASCADE");

        jdbc.update(
                "INSERT INTO categories(id, name, slug, description) VALUES (?, ?, ?, ?)",
                CATEGORY_ID,
                "General",
                "general",
                null
        );

        user = userService.register(
                new RegisterRequest("u@test.com", "author", "12345678")
        );

        moderator = userService.register(
                new RegisterRequest("mod@test.com", "moderator", "12345678")
        );

        stranger = userService.register(
                new RegisterRequest("x@test.com", "stranger", "12345678")
        );
    }

    @Test
    void adminCanPinPost() {
        Post post = postService.createPost(
                user.getId(),
                new CreatePostRequest("Title", "Content", CATEGORY_ID)
        );

        Post pinned = postService.pinPost(post.getId(), Role.ADMIN);

        Assertions.assertTrue(pinned.isPinned());
    }

    @Test
    void moderatorCanPinPost() {
        Post post = postService.createPost(
                user.getId(),
                new CreatePostRequest("Title", "Content", CATEGORY_ID)
        );

        Post pinned = postService.pinPost(post.getId(), Role.MODERATOR);

        Assertions.assertTrue(pinned.isPinned());
    }

    @Test
    void userCannotPinPost() {
        Post post = postService.createPost(
                user.getId(),
                new CreatePostRequest("Title", "Content", CATEGORY_ID)
        );

        Assertions.assertThrows(RuntimeException.class, () -> {
            postService.pinPost(post.getId(), Role.USER);
        });
    }

    @Test
    void adminCanLockPost() {
        Post post = postService.createPost(
                user.getId(),
                new CreatePostRequest("Title", "Content", CATEGORY_ID)
        );

        Post locked = postService.lockPost(post.getId(), Role.ADMIN);

        Assertions.assertTrue(locked.isLocked());
    }

    @Test
    void moderatorCanLockPost() {
        Post post = postService.createPost(
                user.getId(),
                new CreatePostRequest("Title", "Content", CATEGORY_ID)
        );

        Post locked = postService.lockPost(post.getId(), Role.MODERATOR);

        Assertions.assertTrue(locked.isLocked());
    }

    @Test
    void userCannotLockPost() {
        Post post = postService.createPost(
                user.getId(),
                new CreatePostRequest("Title", "Content", CATEGORY_ID)
        );

        Assertions.assertThrows(RuntimeException.class, () -> {
            postService.lockPost(post.getId(), Role.USER);
        });
    }


    // ----------------------------------------------------------

    @Test
    void shouldCreatePost() {

        CreatePostRequest req = new CreatePostRequest(
                "TestTitle",
                "TestContent",
                CATEGORY_ID
        );

        Post post = postService.createPost(user.getId(), req);

        Assertions.assertNotNull(post.getId());
        Assertions.assertEquals("TestTitle", post.getTitle());
        Assertions.assertEquals(CATEGORY_ID, post.getCategoryId().value());

        int count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM posts WHERE id = ?",
                Integer.class,
                post.getId().value()
        );

        Assertions.assertEquals(1, count);
    }

    @Test
    void shouldSearchPostsByTitleOrContent() {

        // Создаём посты
        postService.createPost(user.getId(),
                new CreatePostRequest("Java Spring Guide", "Content A", CATEGORY_ID));

        postService.createPost(user.getId(),
                new CreatePostRequest("Other Title", "Spring Boot tutorial inside", CATEGORY_ID));

        postService.createPost(user.getId(),
                new CreatePostRequest("Random", "Nothing to match", CATEGORY_ID));


        // --- поиск по заголовку ---
        var byTitle = postService.searchPosts("Java", 0, 10);
        Assertions.assertEquals(1, byTitle.size());
        Assertions.assertEquals("Java Spring Guide", byTitle.get(0).getTitle());

        // --- поиск по контенту ---
        var byContent = postService.searchPosts("tutorial", 0, 10);
        Assertions.assertEquals(1, byContent.size());
        Assertions.assertEquals("Other Title", byContent.get(0).getTitle());

        // --- нет совпадений ---
        var empty = postService.searchPosts("xyz123", 0, 10);
        Assertions.assertTrue(empty.isEmpty());
    }

    @Test
    void shouldReturnEmptyListForBlankSearchQuery() {
        var result1 = postService.searchPosts("", 0, 10);
        Assertions.assertTrue(result1.isEmpty());

        var result2 = postService.searchPosts("   ", 0, 10);
        Assertions.assertTrue(result2.isEmpty());

        var result3 = postService.searchPosts(null, 0, 10);
        Assertions.assertTrue(result3.isEmpty());
    }

    @Test
    void shouldReturnPostsByCategorySlug() {

        // Создаём 3 поста в категории "general"
        for (int i = 0; i < 3; i++) {
            postService.createPost(
                    user.getId(),
                    new CreatePostRequest("Title" + i, "Content" + i, CATEGORY_ID)
            );
        }

        // Создаём другую категорию
        jdbc.update(
                "INSERT INTO categories(id, name, slug, description) VALUES (?, ?, ?, ?)",
                "cat-2",
                "Other",
                "other",
                null
        );

        // Создаём 2 поста в категории "other"
        for (int i = 0; i < 2; i++) {
            postService.createPost(
                    user.getId(),
                    new CreatePostRequest("O" + i, "OC" + i, "cat-2")
            );
        }

        // 1. Проверяем category=general
        var generalPosts = postService.getByCategorySlug("general", 0, 10);

        Assertions.assertEquals(3, generalPosts.size());
        Assertions.assertTrue(generalPosts.stream()
                .allMatch(p -> p.getCategoryId().value().equals(CATEGORY_ID)));

        // 2. Проверяем category=other
        var otherPosts = postService.getByCategorySlug("other", 0, 10);

        Assertions.assertEquals(2, otherPosts.size());
        Assertions.assertTrue(otherPosts.stream()
                .allMatch(p -> p.getCategoryId().value().equals("cat-2")));
    }

// ----------------------------------------------------------

    @Test
    void shouldReturnEmptyListForEmptyCategory() {

        // Создаём категорию "empty" без постов
        jdbc.update(
                "INSERT INTO categories(id, name, slug, description) VALUES (?, ?, ?, ?)",
                "cat-empty",
                "Empty",
                "empty",
                "desc"
        );

        var posts = postService.getByCategorySlug("empty", 0, 10);

        Assertions.assertTrue(posts.isEmpty());
    }


    @Test
    void shouldEditOwnPost() {

        Post post = postService.createPost(
                user.getId(),
                new CreatePostRequest("Old", "OldC", CATEGORY_ID)
        );

        UpdatePostRequest upd = new UpdatePostRequest("New", "NewC", null);

        Post updated = postService.editPost(
                post.getId(),
                user.getId(),
                Role.USER,
                upd
        );

        Assertions.assertEquals("New", updated.getTitle());
    }

    // ----------------------------------------------------------

    @Test
    void shouldForbidEditingForeignPost() {

        Post post = postService.createPost(
                user.getId(),
                new CreatePostRequest("Old", "OldC", CATEGORY_ID)
        );

        UpdatePostRequest upd = new UpdatePostRequest("Hack", "Hack", null);

        Assertions.assertThrows(RuntimeException.class, () ->
                postService.editPost(
                        post.getId(),
                        stranger.getId(),
                        Role.USER,
                        upd
                )
        );
    }

    // ----------------------------------------------------------

    @Test
    void moderatorCanEditForeignPost() {

        Post post = postService.createPost(
                user.getId(),
                new CreatePostRequest("Old", "OldC", CATEGORY_ID)
        );

        UpdatePostRequest upd = new UpdatePostRequest("ModEdit", "ModEdit", null);

        Post updated = postService.editPost(
                post.getId(),
                moderator.getId(),
                Role.MODERATOR,
                upd
        );

        Assertions.assertEquals("ModEdit", updated.getTitle());
    }

    // ----------------------------------------------------------

    @Test
    void userCanDeleteOwnPost() {

        Post post = postService.createPost(
                user.getId(),
                new CreatePostRequest("AAA", "BBB", CATEGORY_ID)
        );

        postService.deletePost(
                post.getId(),
                user.getId(),
                Role.USER
        );

        int count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM posts WHERE id = ?",
                Integer.class,
                post.getId().value()
        );

        Assertions.assertEquals(0, count);
    }

    // ----------------------------------------------------------

    @Test
    void shouldForbidDeletingForeignPost() {

        Post post = postService.createPost(
                user.getId(),
                new CreatePostRequest("AAA", "BBB", CATEGORY_ID)
        );

        Assertions.assertThrows(RuntimeException.class, () ->
                postService.deletePost(
                        post.getId(),
                        stranger.getId(),
                        Role.USER
                )
        );
    }

    // ----------------------------------------------------------

    @Test
    void shouldReturnPostById() {
        Post post = postService.createPost(
                user.getId(),
                new CreatePostRequest("AAA", "BBB", CATEGORY_ID)
        );

        Post loaded = postService.getPost(post.getId());

        Assertions.assertEquals(post.getId().value(), loaded.getId().value());
        Assertions.assertEquals(CATEGORY_ID, loaded.getCategoryId().value());
    }

    // ----------------------------------------------------------

    @Test
    void shouldReturnPagedPosts() {

        for (int i = 0; i < 5; i++) {
            postService.createPost(
                    user.getId(),
                    new CreatePostRequest("T" + i, "C" + i, CATEGORY_ID)
            );
        }

        var page = postService.getPaged(0, 3);

        Assertions.assertEquals(3, page.size());
    }
}
