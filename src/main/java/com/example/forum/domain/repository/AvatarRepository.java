package com.example.forum.domain.repository;

import com.example.forum.domain.model.avatar.Avatar;
import com.example.forum.domain.model.avatar.AvatarId;

import java.util.Optional;

public interface AvatarRepository {
    Avatar save(Avatar avatar);
    Optional<Avatar> findById(AvatarId id);
    void delete(AvatarId id);
}