package com.example.forum.domain.model.user;

public class Username {
    private final String value;

    public Username(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (value.length() < 3) {
            throw new IllegalArgumentException("Username too short");
        }
        this.value = value;
    }

    public String get() {
        return value;
    }

}
