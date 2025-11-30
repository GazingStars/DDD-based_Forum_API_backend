package com.example.forum.domain.model.user;

public class Email {
    private final String value;

    public Email(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!value.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.value = value;
    }

    public String get() {
        return value;
    }
}
