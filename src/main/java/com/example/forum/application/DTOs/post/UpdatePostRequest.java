package com.example.forum.application.DTOs.post;

public record UpdatePostRequest(
        String title,
        String content,
        String categoryId
) {}