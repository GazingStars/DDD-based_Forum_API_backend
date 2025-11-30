package com.example.forum.application.services;

import com.example.forum.domain.model.like.*;
import com.example.forum.domain.model.post.PostId;
import com.example.forum.domain.model.comment.CommentId;
import com.example.forum.domain.model.user.UserId;
import com.example.forum.domain.repository.LikeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    private final LikeRepository likes;

    public LikeService(LikeRepository likes) {
        this.likes = likes;
    }

    @Transactional
    public void likePost(UserId userId, PostId postId) {

        LikeTargetId targetId = new LikeTargetId(postId.value());

        if (likes.exists(userId, LikeTargetType.POST, targetId)) {
            return;
        }

        Like like = Like.create(
                userId,
                LikeTargetType.POST,
                targetId
        );

        likes.save(like);
    }

    @Transactional
    public void unlikePost(UserId userId, PostId postId) {

        LikeTargetId targetId = new LikeTargetId(postId.value());

        if (likes.exists(userId, LikeTargetType.POST, targetId)) {
            likes.delete(userId, LikeTargetType.POST, targetId);
        }
    }

    @Transactional
    public long countPostLikes(PostId postId) {
        return likes.count(
                LikeTargetType.POST,
                new LikeTargetId(postId.value())
        );
    }

    @Transactional
    public void likeComment(UserId userId, CommentId commentId) {

        LikeTargetId targetId = new LikeTargetId(commentId.value());

        if (likes.exists(userId, LikeTargetType.COMMENT, targetId)) {
            return;
        }

        Like like = Like.create(
                userId,
                LikeTargetType.COMMENT,
                targetId
        );

        likes.save(like);
    }

    @Transactional
    public void unlikeComment(UserId userId, CommentId commentId) {

        LikeTargetId targetId = new LikeTargetId(commentId.value());

        if (likes.exists(userId, LikeTargetType.COMMENT, targetId)) {
            likes.delete(userId, LikeTargetType.COMMENT, targetId);
        }
    }

    @Transactional
    public long countCommentLikes(CommentId commentId) {
        return likes.count(
                LikeTargetType.COMMENT,
                new LikeTargetId(commentId.value())
        );
    }

    @Transactional
    public boolean hasUserLikedPost(UserId userId, PostId postId) {
        LikeTargetId targetId = new LikeTargetId(postId.value());
        return likes.exists(userId, LikeTargetType.POST, targetId);
    }

    @Transactional
    public boolean hasUserLikedComment(UserId userId, CommentId commentId) {
        LikeTargetId targetId = new LikeTargetId(commentId.value());
        return likes.exists(userId, LikeTargetType.COMMENT, targetId);
    }
}
