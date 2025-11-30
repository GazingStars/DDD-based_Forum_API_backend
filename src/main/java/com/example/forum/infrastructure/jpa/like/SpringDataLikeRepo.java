package com.example.forum.infrastructure.jpa.like;


import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataLikeRepo extends JpaRepository<LikeEntity, String> {

    boolean existsByUserIdAndTargetTypeAndTargetId(String userId, String targetType, String targetId);

    void deleteByUserIdAndTargetTypeAndTargetId(String userId, String targetType, String targetId);

    long countByTargetTypeAndTargetId(String targetType, String targetId);
}
