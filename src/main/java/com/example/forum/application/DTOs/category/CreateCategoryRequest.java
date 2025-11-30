package com.example.forum.application.DTOs.category;

public record CreateCategoryRequest(
        String name,
        String slug,
        String description
) {}
