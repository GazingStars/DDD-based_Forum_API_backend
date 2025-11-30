package com.example.forum.web.Controllers;

import com.example.forum.application.DTOs.post.PostResponse;
import com.example.forum.application.DTOs.user.ChangeRoleRequest;
import com.example.forum.application.services.PostService;
import com.example.forum.application.services.UserService;

import com.example.forum.domain.model.post.PostId;
import com.example.forum.domain.model.user.UserId;
import com.example.forum.infrastructure.security.AuthUserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final PostService posts;
    private final UserService users;

    public AdminController(PostService posts, UserService users) {
        this.posts = posts;
        this.users = users;
    }


    @PostMapping("/posts/{id}/pin")
    public PostResponse pin(
            @PathVariable String id,
            @AuthenticationPrincipal AuthUserPrincipal principal
    ) {
        var post = posts.pinPost(new PostId(id), principal.getRole());
        return PostResponse.from(post, 0, false);
    }


    @PostMapping("/posts/{id}/lock")
    public PostResponse lock(
            @PathVariable String id,
            @AuthenticationPrincipal AuthUserPrincipal principal
    ) {
        var post = posts.lockPost(new PostId(id), principal.getRole());
        return PostResponse.from(post, 0, false);
    }


    @DeleteMapping("/users/{id}")
    public void ban(
            @PathVariable String id,
            @AuthenticationPrincipal AuthUserPrincipal principal
    ) {
        users.banUser(new UserId(id), principal.getRole());
    }


    @PutMapping("/users/{id}/role")
    public void changeRole(
            @PathVariable String id,
            @RequestBody ChangeRoleRequest req,
            @AuthenticationPrincipal AuthUserPrincipal principal
    ) {
        users.changeRole(new UserId(id), req.role(), principal.getRole());
    }
}
