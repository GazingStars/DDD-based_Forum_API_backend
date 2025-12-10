package com.example.forum.web.DTOs.user;

import com.example.forum.domain.model.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public record UserResponse(
        String id,
        String email,
        String username,
        String role,
        String avatarId,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Instant createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Instant updatedAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId().get(),
                user.getEmail().get(),
                user.getUsername().get(),
                user.getRole().name(),
                user.getAvatarId() != null ? user.getAvatarId().value() : null,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

}
