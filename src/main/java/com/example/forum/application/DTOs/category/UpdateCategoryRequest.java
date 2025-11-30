package com.example.forum.application.DTOs.category;

public record UpdateCategoryRequest(
        String name,
        String slug,
        String description
) {}
