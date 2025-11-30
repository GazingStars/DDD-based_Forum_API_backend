package com.example.forum.domain.model.like;

public record LikeTargetId(String value) {
    public LikeTargetId {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("TargetId cannot be empty");
    }
}
