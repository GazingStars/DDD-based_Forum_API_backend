package com.example.forum.web.Controllers;

import com.example.forum.application.services.LikeService;
import com.example.forum.application.services.UserService;
import com.example.forum.domain.model.user.UserId;
import com.example.forum.infrastructure.security.AuthUserPrincipal;
import com.example.forum.web.DTOs.user.UserResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.forum.application.DTOs.post.PostResponse;
import com.example.forum.application.services.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final PostService posts;
    private final UserService users;
    private final LikeService likeService;

    public SearchController(PostService posts, UserService users, LikeService likeService) {
        this.posts = posts;
        this.users = users;
        this.likeService = likeService;
    }

    @GetMapping("/posts")
    public List<PostResponse> searchPosts(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal AuthUserPrincipal principal
    ) {
        return posts.searchPosts(query, page, size)
                .stream()
                .map(post -> {
                    long likes = likeService.countPostLikes(post.getId());

                    boolean likedByMe = false;
                    if (principal != null) {
                        likedByMe = likeService.hasUserLikedPost(
                                new UserId(principal.getUserId()),
                                post.getId()
                        );
                    }

                    return PostResponse.from(post, likes, likedByMe);
                })
                .toList();
    }

    @GetMapping("/users")
    public List<UserResponse> searchUsers(@RequestParam String query) {
        return users.searchUsers(query)
                .stream()
                .map(UserResponse::from)
                .toList();
    }
}
