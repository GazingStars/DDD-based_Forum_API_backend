package com.example.forum.application.services.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorage {

    String saveAvatar(String userId, MultipartFile file) throws IOException;

    void delete(String url);
}
