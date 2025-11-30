package com.example.forum.domain.model.like;


import com.example.forum.domain.model.user.UserId;

import java.time.Instant;

public class Like {

    private final LikeId id;
    private final UserId userId;
    private final LikeTargetType targetType;
    private final LikeTargetId targetId;
    private final Instant createdAt;

    public Like(LikeId id,
                UserId userId,
                LikeTargetType targetType,
                LikeTargetId targetId,
                Instant createdAt) {

        this.id = id;
        this.userId = userId;
        this.targetType = targetType;
        this.targetId = targetId;
        this.createdAt = createdAt;
    }

    public static Like create(UserId userId,
                              LikeTargetType targetType,
                              LikeTargetId targetId) {

        return new Like(
                LikeId.generate(),
                userId,
                targetType,
                targetId,
                Instant.now()
        );
    }

    public LikeId id() { return id; }
    public UserId userId() { return userId; }
    public LikeTargetType targetType() { return targetType; }
    public LikeTargetId targetId() { return targetId; }
    public Instant createdAt() { return createdAt; }
}