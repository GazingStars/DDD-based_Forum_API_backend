package com.example.forum.infrastructure.jpa.comment;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;

@Entity
@Table(name = "comments")
public class CommentEntity {

    @Id
    @Getter
    private String id;

    @Getter
    @Column(nullable = false)
    private String postId;

    @Getter
    @Column(nullable = false)
    private String authorId;

    @Getter
    @Column(nullable = false, length = 5000)
    private String content;

    @Getter
    private Instant createdAt;

    @Getter
    private Instant updatedAt;

    public CommentEntity() {
    }

    public CommentEntity(String id, String postId, String authorId, String content, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.postId = postId;
        this.authorId = authorId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
