package com.example.forum.domain.model.post;

public record PostContent(String value) {

    public PostContent {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Post content cannot be empty");
        }
        if (value.length() > 10_000) {
            throw new IllegalArgumentException("Post content too long");
        }
    }
}