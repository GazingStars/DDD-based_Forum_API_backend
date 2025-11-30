package com.example.forum.domain.model.comment;

import com.example.forum.domain.model.post.PostId;
import com.example.forum.domain.model.user.Role;
import com.example.forum.domain.model.user.UserId;

import java.time.Instant;

public class Comment {
    private final CommentId id;
    private final PostId postId;
    private final UserId authorId;

    private CommentContent content;
    private final Instant createdAt;
    private Instant updatedAt;

    public Comment(CommentId id, PostId postId, UserId authorId, CommentContent content) {
        this.id = id;
        this.postId = postId;
        this.authorId = authorId;
        this.content = content;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public Comment(CommentId id, PostId postId, UserId authorId, CommentContent content, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.postId = postId;
        this.authorId = authorId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void edit(UserId editor, CommentContent newContent) {
        if (!editor.equals(this.authorId)) {
            throw new IllegalStateException("Only author can edit comment");
        }
        this.content = newContent;
        this.updatedAt = Instant.now();
    }

    public void delete(UserId user, Role role) {
        if (!user.equals(authorId) && role != Role.MODERATOR && role != Role.ADMIN) {
            throw new IllegalStateException("Not allowed to delete comment");
        }
    }

    public CommentId id() {
        return id;
    }

    public PostId postId() {
        return postId;
    }

    public UserId authorId() {
        return authorId;
    }

    public CommentContent content() {
        return content;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

}
