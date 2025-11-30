package com.example.forum.domain.repository;

import com.example.forum.domain.model.like.Like;
import com.example.forum.domain.model.like.LikeTargetId;
import com.example.forum.domain.model.like.LikeTargetType;
import com.example.forum.domain.model.user.UserId;

public interface LikeRepository {
    Like save(Like like);
    void delete(UserId userId, LikeTargetType type, LikeTargetId targetId);
    boolean exists(UserId u, LikeTargetType type, LikeTargetId id);
    long count(LikeTargetType type, LikeTargetId id);
}