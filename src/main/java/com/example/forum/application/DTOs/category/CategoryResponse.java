package com.example.forum.application.DTOs.category;


import java.time.Instant;

public record CategoryResponse(
        String id,
        String name,
        String slug,
        String description,
        Instant createdAt,
        Instant updatedAt
) {}