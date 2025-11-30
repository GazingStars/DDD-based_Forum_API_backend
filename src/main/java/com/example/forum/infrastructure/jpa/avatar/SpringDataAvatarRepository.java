package com.example.forum.infrastructure.jpa.avatar;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataAvatarRepository extends JpaRepository<AvatarEntity, String> {
}