package com.example.forum.application;

import com.example.forum.application.DTOs.category.CreateCategoryRequest;
import com.example.forum.application.DTOs.category.UpdateCategoryRequest;
import com.example.forum.application.DTOs.category.CategoryResponse;
import com.example.forum.application.services.CategoryService;
import com.example.forum.domain.repository.CategoryRepository;

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
public class CategoryServiceTest {

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
    private CategoryService service;

    @Autowired
    private CategoryRepository repo;

    @Autowired
    JdbcTemplate jdbc;

    @BeforeEach
    void cleanDb() {
        jdbc.execute("TRUNCATE TABLE posts, categories CASCADE");
    }

    // -------------------------------------------------------------
    @Test
    void shouldCreateCategory() {
        CreateCategoryRequest req = new CreateCategoryRequest(
                "Programming",
                "programming",
                "All about software development"
        );

        CategoryResponse res = service.create(req);

        Assertions.assertNotNull(res.id());
        Assertions.assertEquals("Programming", res.name());
        Assertions.assertEquals("programming", res.slug());
        Assertions.assertEquals("All about software development", res.description());
    }

    // -------------------------------------------------------------
    @Test
    void shouldRejectDuplicateSlug() {

        CreateCategoryRequest req1 = new CreateCategoryRequest(
                "Java",
                "java",
                "Java topics"
        );

        CreateCategoryRequest req2 = new CreateCategoryRequest(
                "JAVA 2",
                "java",
                "Duplicate slug"
        );

        service.create(req1);

        assertThrows(IllegalStateException.class, () -> {
            service.create(req2);
        });
    }

    // -------------------------------------------------------------
    @Test
    void shouldUpdateCategory() {

        var created = service.create(new CreateCategoryRequest(
                "Art",
                "art",
                "Drawing and design"
        ));

        var updated = service.update(created.id(), new UpdateCategoryRequest(
                "Digital Art",
                "digital-art",
                "Updated category"
        ));

        Assertions.assertEquals("Digital Art", updated.name());
        Assertions.assertEquals("digital-art", updated.slug());
        Assertions.assertEquals("Updated category", updated.description());
    }

    // -------------------------------------------------------------
    @Test
    void shouldGetBySlug() {

        service.create(new CreateCategoryRequest(
                "Medicine",
                "medicine",
                "All medical topics"
        ));

        var res = service.getBySlug("medicine");

        Assertions.assertEquals("Medicine", res.name());
        Assertions.assertEquals("medicine", res.slug());
    }

    // -------------------------------------------------------------
    @Test
    void shouldDeleteCategory() {

        var created = service.create(new CreateCategoryRequest(
                "Science",
                "science",
                "Scientific topics"
        ));

        service.delete(created.id());

        assertThrows(IllegalStateException.class, () -> {
            service.getById(created.id());
        });
    }
}
