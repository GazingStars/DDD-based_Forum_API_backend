package com.example.forum.application.DTOs.user;

public record UpdateUserRequest(
        String email,
        String username,
        String newPassword
) {}