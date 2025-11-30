package com.example.forum.application.storage;

import com.example.forum.application.services.storage.FileStorage;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;

@Component
public class FakeFileStorage {

    public Set<String> saved = new HashSet<>();
    public Set<String> deleted = new HashSet<>();


    public String saveAvatar(String userId, MultipartFile file) {
        String url = "/static/avatars/" + userId + "/" + file.getOriginalFilename();
        saved.add(url);
        return url;
    }

    public void delete(String url) {
        deleted.add(url);
    }
}