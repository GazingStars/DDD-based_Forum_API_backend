package com.example.forum.application.services.storage;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
public class LocalFileStorage implements FileStorage {

    private static final String ROOT = "uploads/avatars";

    @Override
    public String saveAvatar(String userId, MultipartFile file) throws IOException {

        Path userDir = Path.of(ROOT, userId);
        Files.createDirectories(userDir);

        String ext = getExtension(file.getOriginalFilename());

        String safeName = System.currentTimeMillis() + "." + ext;

        Path target = userDir.resolve(safeName);

        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return "/static/avatars/" + userId + "/" + safeName;
    }

    @Override
    public void delete(String url) {

        if (url == null) return;

        String fileSystemPath = url.replace("/static/avatars", ROOT);

        File f = new File(fileSystemPath);
        if (f.exists()) {
            f.delete();
        }
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new IllegalArgumentException("Invalid filename");
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }
}
