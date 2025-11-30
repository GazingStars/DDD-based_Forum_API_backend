package com.example.forum.domain.model.comment;

import java.util.UUID;

public record CommentId(String value) {
    public CommentId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Invalid comment id");
        }
    }

    public static CommentId newId() {
        return new CommentId(UUID.randomUUID().toString());
    }
}