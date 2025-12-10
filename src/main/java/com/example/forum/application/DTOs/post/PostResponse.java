package com.example.forum.application.DTOs.post;

import com.example.forum.domain.model.post.Post;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public record PostResponse(
        String id,
        String authorId,
        String title,
        String content,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Instant createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Instant editedAt,
        String categoryId,
        long likes,
        boolean likedByMe
) {
    public static PostResponse from(
            Post post,
            long likeCount,
            boolean likedByMe
    ) {
        return new PostResponse(
                post.getId().value(),
                post.getAuthorId().get(),
                post.getTitle(),
                post.getContent().value(),
                post.getCreatedAt(),
                post.getEditedAt(),
                post.getCategoryId().value(),
                likeCount,
                likedByMe
        );
    }
}
