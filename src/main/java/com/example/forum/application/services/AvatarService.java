package com.example.forum.application.services;

import com.example.forum.application.services.storage.FileStorage;
import com.example.forum.domain.model.avatar.Avatar;
import com.example.forum.domain.model.user.UserId;
import com.example.forum.domain.repository.AvatarRepository;
import com.example.forum.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AvatarService {

    private final AvatarRepository avatarRepository;
    private final UserRepository userRepository;
    private final FileStorage fileStorage;

    public AvatarService(
            AvatarRepository avatarRepository,
            UserRepository userRepository,
            FileStorage fileStorage
    ) {
        this.avatarRepository = avatarRepository;
        this.userRepository = userRepository;
        this.fileStorage = fileStorage;
    }

    @Transactional
    public Avatar upload(UserId userId, MultipartFile file) throws IOException {

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        if (user.getAvatarId() != null) {
            avatarRepository.findById(user.getAvatarId())
                    .ifPresent(old -> {
                        fileStorage.delete(old.getUrl());
                        avatarRepository.delete(old.getId());
                    });
        }

        String url = fileStorage.saveAvatar(userId.get(), file);

        Avatar avatar = Avatar.create(
                file.getOriginalFilename(),
                file.getSize(),
                url
        );

        avatar = avatarRepository.save(avatar);

        user.assignAvatar(avatar.getId());
        userRepository.save(user);

        return avatar;
    }

    @Transactional
    public void delete(UserId userId) {

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        if (user.getAvatarId() == null) {
            return;
        }

        var avatarId = user.getAvatarId();

        avatarRepository.findById(avatarId).ifPresent(avatar -> {
            fileStorage.delete(avatar.getUrl());

            avatarRepository.delete(avatarId);
        });

        user.assignAvatar(null);
        userRepository.save(user);
    }
}
