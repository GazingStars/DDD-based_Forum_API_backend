package com.example.forum.domain.service;

import com.example.forum.domain.model.post.Post;
import com.example.forum.domain.model.user.Role;
import com.example.forum.domain.model.user.UserId;
import org.springframework.stereotype.Service;

@Service
public class PostDomainService {

    public boolean canEdit(Post post, UserId userId, Role role) {
        if (post.getAuthorId().equals(userId)) return true;

        return role == Role.ADMIN || role == Role.MODERATOR;
    }

    public boolean canDelete(Post post, UserId userId, Role role) {
        if (post.getAuthorId().equals(userId)) return true;

        return role == Role.ADMIN;
    }

    public boolean requiresModeratorReview(Post post) {
        return post.getTitle().contains("запрещённое слово")
               || post.getContent().value().contains("мат");
    }
}
