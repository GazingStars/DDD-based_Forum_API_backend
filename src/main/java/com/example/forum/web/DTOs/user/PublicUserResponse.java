package com.example.forum.web.DTOs.user;

import com.example.forum.domain.model.user.User;

public record PublicUserResponse(
        String username,
        String role
) {
    public static PublicUserResponse from(User user) {
        return new PublicUserResponse(
                user.getUsername().get(),
                user.getRole().name()
        );
    }
}
