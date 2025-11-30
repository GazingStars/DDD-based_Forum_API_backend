package com.example.forum.domain.model.post;

import java.util.UUID;

public record PostId(String value) {

    public PostId {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("Invalid PostId");
    }

    public static PostId generate() {
        return new PostId(UUID.randomUUID().toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostId)) return false;
        PostId other = (PostId) o;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}