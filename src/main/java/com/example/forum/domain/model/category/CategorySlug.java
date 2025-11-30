package com.example.forum.domain.model.category;

public record CategorySlug(String value) {

    private static final String REGEX = "^[a-z0-9]+(?:-[a-z0-9]+)*$";

    public CategorySlug {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("Slug cannot be empty");

        if (!value.matches(REGEX))
            throw new IllegalArgumentException("Invalid slug format");
    }
}
