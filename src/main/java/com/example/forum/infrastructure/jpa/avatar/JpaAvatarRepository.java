package com.example.forum.infrastructure.jpa.avatar;

import com.example.forum.domain.model.avatar.Avatar;
import com.example.forum.domain.model.avatar.AvatarId;
import com.example.forum.domain.repository.AvatarRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaAvatarRepository implements AvatarRepository {
    private final SpringDataAvatarRepository repo;

    public JpaAvatarRepository(SpringDataAvatarRepository repo) {
        this.repo = repo;
    }

    @Override
    public Avatar save(Avatar avatar) {
        AvatarEntity entity = AvatarMapper.toEntity(avatar);
        AvatarEntity saved = repo.save(entity);
        return AvatarMapper.toDomain(saved);
    }

    @Override
    public Optional<Avatar> findById(AvatarId id) {
        return repo.findById(id.value())
                .map(AvatarMapper::toDomain);
    }

    @Override
    public void delete(AvatarId id) {
        repo.deleteById(id.value());
    }
}
