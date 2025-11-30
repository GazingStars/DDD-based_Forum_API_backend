package com.example.forum.application.DTOs.avatar;

import org.springframework.web.multipart.MultipartFile;

public record AvatarUploadRequest(
        MultipartFile file
) {}
