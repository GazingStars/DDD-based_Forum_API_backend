package com.example.forum.domain.model.avatar;

import java.util.UUID;

public record AvatarId(String value) {

    public AvatarId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("AvatarId cannot be empty");
        }
    }

    public static AvatarId generate() {
        return new AvatarId(UUID.randomUUID().toString());
    }
}
