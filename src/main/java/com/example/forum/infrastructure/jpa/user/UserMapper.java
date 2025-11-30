package com.example.forum.infrastructure.jpa.user;

import com.example.forum.domain.model.avatar.AvatarId;
import com.example.forum.domain.model.user.Email;
import com.example.forum.domain.model.user.User;
import com.example.forum.domain.model.user.UserId;
import com.example.forum.domain.model.user.Username;

public class UserMapper {

    public static UserEntity toEntity(User user) {

        return new UserEntity(
                user.getId().get(),
                user.getEmail().get(),
                user.getUsername().get(),
                user.getPasswordHash(),
                user.getRole(),
                user.isBlocked(),
                user.isDeleted(),
                user.getAvatarId() != null
                        ? user.getAvatarId().value()
                        : null,
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getLastLoginAt()
        );
    }


    public static User toDomain(UserEntity entity) {

        return new User(
                new UserId(entity.getId()),
                new Email(entity.getEmail()),
                new Username(entity.getUsername()),
                entity.getPasswordHash(),
                entity.getRole(),

                entity.getAvatarId() != null
                        ? new AvatarId(entity.getAvatarId())
                        : null,

                entity.getIsBlocked(),
                entity.getIsDeleted(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getLastLoginAt()
        );
    }
}
