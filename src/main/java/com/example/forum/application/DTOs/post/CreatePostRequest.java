package com.example.forum.application.DTOs.post;

public record CreatePostRequest(
        String title,
        String content,
        String categoryId
) {}
