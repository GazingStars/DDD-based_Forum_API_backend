package com.example.forum.domain.model.category;

import java.util.UUID;

public record CategoryId(String value) {

    public CategoryId {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("CategoryId cannot be empty");

    }

    public static CategoryId generate() {
        return new CategoryId(UUID.randomUUID().toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryId)) return false;

        CategoryId other = (CategoryId) o;
        return value.equals(other.value);
    }

}
