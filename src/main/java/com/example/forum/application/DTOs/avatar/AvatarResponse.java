package com.example.forum.application.DTOs.avatar;


import com.example.forum.domain.model.avatar.Avatar;

public record AvatarResponse(
        String id,
        String fileName,
        String url,
        String uploadedAt
) {
    public static AvatarResponse from(Avatar avatar) {
        return new AvatarResponse(
                avatar.getId().value(),
                avatar.getFileName(),
                avatar.getUrl(),
                avatar.getUploadedAt().toString()
        );
    }
}
