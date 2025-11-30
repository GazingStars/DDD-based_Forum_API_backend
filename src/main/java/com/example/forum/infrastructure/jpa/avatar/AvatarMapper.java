package com.example.forum.infrastructure.jpa.avatar;

import com.example.forum.domain.model.avatar.Avatar;
import com.example.forum.domain.model.avatar.AvatarId;

public class AvatarMapper {

    public static AvatarEntity toEntity(Avatar avatar) {
        AvatarEntity entity = new AvatarEntity();
        entity.setId(avatar.getId().value());
        entity.setFileName(avatar.getFileName());
        entity.setUrl(avatar.getUrl());
        entity.setUploadedAt(avatar.getUploadedAt());
        return entity;
    }

    public static Avatar toDomain(AvatarEntity entity) {
        return new Avatar(
                new AvatarId(entity.getId()),
                entity.getFileName(),
                entity.getUrl(),
                entity.getUploadedAt()
        );
    }
}
