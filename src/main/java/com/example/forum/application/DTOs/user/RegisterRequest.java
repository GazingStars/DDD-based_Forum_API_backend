package com.example.forum.application.DTOs.user;

public record RegisterRequest(
        String email,
        String username,
        String password
) {}

