package com.example.forum.domain;

import com.example.forum.domain.model.category.CategoryId;
import com.example.forum.domain.model.post.Post;
import com.example.forum.domain.model.post.PostContent;
import com.example.forum.domain.model.post.PostId;
import com.example.forum.domain.model.user.UserId;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class PostTest {

    @Test
    void shouldCreateValidPost() {
        UserId author = new UserId("user-1");
        PostContent content = new PostContent("some content");
        CategoryId category = new CategoryId("cat-1");

        Post post = Post.create(author, "My title", content, category);

        assertNotNull(post.getId());
        assertEquals("My title", post.getTitle());
        assertEquals("some content", post.getContent().value());
        assertEquals(author, post.getAuthorId());
        assertEquals(category, post.getCategoryId());
        assertFalse(post.isPinned());
        assertFalse(post.isLocked());
        assertNotNull(post.getCreatedAt());
        assertNull(post.getEditedAt());
    }

    @Test
    void shouldThrowWhenTitleEmpty() {
        UserId author = new UserId("u1");
        PostContent content = new PostContent("abc");
        CategoryId category = new CategoryId("cat");

        assertThrows(
                IllegalArgumentException.class,
                () -> Post.create(author, "   ", content, category)
        );
    }

    @Test
    void shouldThrowWhenContentEmpty() {
        UserId author = new UserId("u1");
        CategoryId category = new CategoryId("cat");

        assertThrows(
                IllegalArgumentException.class,
                () -> Post.create(author, "Title", new PostContent("   "), category)
        );
    }

    @Test
    void shouldEditPostCorrectly() {
        UserId author = new UserId("user");
        CategoryId category = new CategoryId("cat");

        Post post = Post.create(author, "Old title", new PostContent("old content"), category);

        post.edit("New title", new PostContent("new content"), new CategoryId("Dog"));

        assertEquals("New title", post.getTitle());
        assertEquals("new content", post.getContent().value());
        assertEquals("Dog", post.getCategoryId().value());
        assertNotNull(post.getEditedAt());
    }

    @Test
    void shouldThrowWhenEditingWithInvalidTitle() {
        UserId author = new UserId("u1");
        CategoryId category = new CategoryId("cat");

        Post post = Post.create(
                author,
                "Initial",
                new PostContent("content"),
                category
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> post.edit("   ", new PostContent("zzz"), category)
        );
    }

    @Test
    void shouldRestoreFromPersistenceConstructor() {
        PostId id = new PostId("post-1");
        UserId author = new UserId("user-1");
        CategoryId category = new CategoryId("cat-1");

        Instant created = Instant.now();
        Instant edited = Instant.now();

        Post post = new Post(
                id,
                author,
                "Title",
                new PostContent("content"),
                category,
                true,
                false,
                created,
                edited
        );

        assertEquals(id, post.getId());
        assertEquals(author, post.getAuthorId());
        assertEquals(category, post.getCategoryId());
        assertEquals("Title", post.getTitle());
        assertEquals("content", post.getContent().value());
        assertTrue(post.isPinned());
        assertFalse(post.isLocked());
        assertEquals(created, post.getCreatedAt());
        assertEquals(edited, post.getEditedAt());
    }
}
