package com.example.forum.web.DTOs.post;

import com.example.forum.domain.model.post.Post;

public record PostResponse(
        String id,
        String authorId,
        String title,
        String content,
        String createdAt,
        String editedAt
) {
    public static PostResponse from(Post p) {
        return new PostResponse(
                p.getId().value(),
                p.getAuthorId().get(),
                p.getTitle(),
                p.getContent().value(),
                p.getCreatedAt().toString(),
                p.getEditedAt() == null ? null : p.getEditedAt().toString()
        );
    }
}