package com.example.forum.infrastructure.jpa.like;


import com.example.forum.domain.model.like.Like;
import com.example.forum.domain.model.like.LikeId;
import com.example.forum.domain.model.like.LikeTargetId;
import com.example.forum.domain.model.like.LikeTargetType;
import com.example.forum.domain.model.user.UserId;

public class LikeMapper {

    public static LikeEntity toEntity(Like like) {
        return new LikeEntity(
                like.id().value(),
                like.userId().get(),
                like.targetType().name(),
                like.targetId().value(),
                like.createdAt()
        );
    }

    public static Like toDomain(LikeEntity e) {
        return new Like(
                new LikeId(e.getId()),
                new UserId(e.getUserId()),
                LikeTargetType.valueOf(e.getTargetType()),
                new LikeTargetId(e.getTargetId()),
                e.getCreatedAt()
        );
    }
}