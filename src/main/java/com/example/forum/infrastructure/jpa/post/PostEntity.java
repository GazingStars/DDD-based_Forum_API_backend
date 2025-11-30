package com.example.forum.infrastructure.jpa.post;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;

@Entity
@Table(name = "posts")
public class PostEntity {

    @Id
    @Getter
    private String id;

    @Getter
    @Column(name = "author_id", nullable = false)
    private String authorId;

    @Getter
    @Column(nullable = false)
    private String title;

    @Getter
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Getter
    @Column(name = "category_id")
    private String categoryId;

    @Getter
    @Column(name = "is_pinned", nullable = false)
    private boolean pinned;

    @Getter
    @Column(name = "is_locked", nullable = false)
    private boolean locked;

    @Getter
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Getter
    @Column(name = "edited_at")
    private Instant editedAt;

    protected PostEntity() {
    }

    public PostEntity(
            String id,
            String authorId,
            String title,
            String content,
            String categoryId,
            boolean pinned,
            boolean locked,
            Instant createdAt,
            Instant editedAt
    ) {
        this.id = id;
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
        this.pinned = pinned;
        this.locked = locked;
        this.createdAt = createdAt;
        this.editedAt = editedAt;
    }
}
