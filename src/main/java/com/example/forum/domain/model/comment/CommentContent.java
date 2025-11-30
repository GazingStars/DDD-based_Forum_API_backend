package com.example.forum.domain.model.comment;

public record CommentContent(String value) {
    public CommentContent {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Comment cannot be empty");
        }
        if (value.length() > 5000) {
            throw new IllegalArgumentException("Comment too long");
        }
    }
}