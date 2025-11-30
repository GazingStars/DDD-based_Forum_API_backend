package com.example.forum.web.DTOs.comment;

import com.example.forum.domain.model.comment.Comment;

import java.time.Instant;

public record CommentResponse(
        String id,
        String postId,
        String authorId,
        String content,
        Instant createdAt,
        Instant updatedAt,
        long likeCount,
        boolean likedByMe
) {
    public static CommentResponse from(Comment c, long likeCount, boolean likedByMe) {
        return new CommentResponse(
                c.id().value(),
                c.postId().value(),
                c.authorId().get(),
                c.content().value(),
                c.createdAt(),
                c.updatedAt(),
                likeCount,
                likedByMe
        );
    }
}