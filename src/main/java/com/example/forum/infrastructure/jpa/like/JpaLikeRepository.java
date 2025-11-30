package com.example.forum.infrastructure.jpa.like;

import com.example.forum.domain.model.like.Like;
import com.example.forum.domain.model.like.LikeTargetId;
import com.example.forum.domain.model.like.LikeTargetType;
import com.example.forum.domain.model.user.UserId;
import com.example.forum.domain.repository.LikeRepository;
import org.springframework.stereotype.Repository;

@Repository
public class JpaLikeRepository implements LikeRepository {
    private final SpringDataLikeRepo repo;

    public JpaLikeRepository(SpringDataLikeRepo repo) {
        this.repo = repo;
    }

    @Override
    public Like save(Like like) {
        LikeEntity saved = repo.save(LikeMapper.toEntity(like));
        return LikeMapper.toDomain(saved);
    }

    @Override
    public void delete(UserId userId, LikeTargetType type, LikeTargetId targetId) {
        repo.deleteByUserIdAndTargetTypeAndTargetId(
                userId.get(),
                type.name(),
                targetId.value()
        );
    }

    @Override
    public boolean exists(UserId userId, LikeTargetType type, LikeTargetId targetId) {
        return repo.existsByUserIdAndTargetTypeAndTargetId(
                userId.get(),
                type.name(),
                targetId.value()
        );
    }

    @Override
    public long count(LikeTargetType type, LikeTargetId targetId) {
        return repo.countByTargetTypeAndTargetId(
                type.name(),
                targetId.value()
        );
    }
}
