package com.example.forum.domain.model.like;

import java.util.UUID;

public record LikeId(String value) {

    public LikeId {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("LikeId cannot be empty");
    }

    public static LikeId generate() {
        return new LikeId(UUID.randomUUID().toString());
    }
}

