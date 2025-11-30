package com.example.forum.domain;

import com.example.forum.domain.model.comment.Comment;
import com.example.forum.domain.model.comment.CommentContent;
import com.example.forum.domain.model.comment.CommentId;
import com.example.forum.domain.model.post.PostId;
import com.example.forum.domain.model.user.Role;
import com.example.forum.domain.model.user.UserId;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    @Test
    void authorCanEditComment() {
        UserId author = new UserId("author-1");
        Comment comment = new Comment(
                CommentId.newId(),
                new PostId("post-1"),
                author,
                new CommentContent("old content")
        );

        Instant before = comment.updatedAt();

        System.out.println("Created: " + comment.createdAt());
        System.out.println("Updated (before): " + comment.updatedAt());

        comment.edit(author, new CommentContent("new content"));

        System.out.println("Updated (after): " + comment.updatedAt());

        assertEquals("new content", comment.content().value());
        assertTrue(comment.updatedAt().isAfter(before));
    }

    @Test
    void nonAuthorCannotEditComment() {
        UserId author = new UserId("author-1");
        UserId otherUser = new UserId("user-2");

        Comment comment = new Comment(
                CommentId.newId(),
                new PostId("post-1"),
                author,
                new CommentContent("content")
        );

        assertThrows(IllegalStateException.class, () ->
                comment.edit(otherUser, new CommentContent("hacked content"))
        );
    }

    @Test
    void authorCanDeleteCommentWithUserRole() {
        UserId author = new UserId("author-1");
        Comment comment = new Comment(
                CommentId.newId(),
                new PostId("post-1"),
                author,
                new CommentContent("content")
        );

        assertDoesNotThrow(() ->
                comment.delete(author, Role.USER)
        );
    }

    @Test
    void moderatorCanDeleteAnyComment() {
        UserId author = new UserId("author-1");
        UserId moderator = new UserId("mod-1");

        Comment comment = new Comment(
                CommentId.newId(),
                new PostId("post-1"),
                author,
                new CommentContent("content")
        );

        assertDoesNotThrow(() ->
                comment.delete(moderator, Role.MODERATOR)
        );
    }

    @Test
    void adminCanDeleteAnyComment() {
        UserId author = new UserId("author-1");
        UserId admin = new UserId("admin-1");

        Comment comment = new Comment(
                CommentId.newId(),
                new PostId("post-1"),
                author,
                new CommentContent("content")
        );

        assertDoesNotThrow(() ->
                comment.delete(admin, Role.ADMIN)
        );
    }

    @Test
    void nonAuthorUserCannotDeleteComment() {
        UserId author = new UserId("author-1");
        UserId otherUser = new UserId("user-2");

        Comment comment = new Comment(
                CommentId.newId(),
                new PostId("post-1"),
                author,
                new CommentContent("content")
        );

        assertThrows(IllegalStateException.class, () ->
                comment.delete(otherUser, Role.USER)
        );
    }

    @Test
    void secondaryConstructorKeepsCreatedAndUpdated() {
        Instant created = Instant.parse("2025-01-01T10:00:00Z");
        Instant updated = Instant.parse("2025-01-01T11:00:00Z");

        Comment comment = new Comment(
                CommentId.newId(),
                new PostId("post-1"),
                new UserId("author-1"),
                new CommentContent("content"),
                created,
                updated
        );

        assertEquals(created, comment.createdAt());
        assertEquals(updated, comment.updatedAt());
    }
}
