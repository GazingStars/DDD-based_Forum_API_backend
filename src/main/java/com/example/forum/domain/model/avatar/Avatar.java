package com.example.forum.domain.model.avatar;

import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Avatar {

    private final AvatarId id;
    private final String fileName;
    private final String url;
    private final Instant uploadedAt;

    private static final List<String> ALLOWED_EXTENSIONS = List.of("png", "jpg", "jpeg", "webp");
    private static final long MAX_SIZE = 5 * 1024 * 1024;

    public Avatar(AvatarId id, String fileName, String url, Instant uploadedAt) {
        validateExtension(fileName);
        validateUrl(url);
        this.id = id;
        this.fileName = fileName;
        this.url = url;
        this.uploadedAt = uploadedAt;
    }

    public static Avatar create(String originalName, long size, String storageUrl) {

        validateFileSize(size);
        validateExtension(originalName);

        String cleanName = generateSafeFileName(originalName);

        return new Avatar(
                AvatarId.generate(),
                cleanName,
                storageUrl,
                Instant.now()
        );
    }

    private static void validateExtension(String fileName) {
        String ext = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            throw new IllegalArgumentException("Unsupported avatar extension: " + ext);
        }
    }

    private static void validateFileSize(long size) {
        if (size > MAX_SIZE) {
            throw new IllegalArgumentException("Avatar too large");
        }
    }

    private static void validateUrl(String url) {
        if (url.startsWith("http://")) {
            throw new IllegalArgumentException("Avatar URL must be https");
        }
    }

    private static String generateSafeFileName(String original) {
        return UUID.randomUUID() + "-" + original.replaceAll("[^a-zA-Z0-9.\\-]", "_");
    }

}


