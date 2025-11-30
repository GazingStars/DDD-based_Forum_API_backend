package com.example.forum.domain.model.category;

public record CategoryName(String value) {

    public CategoryName {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("Category name cannot be empty");

        if (value.length() > 255)
            throw new IllegalArgumentException("Category name is too long");
    }
}
