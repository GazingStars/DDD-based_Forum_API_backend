package com.example.forum.web.controllers;

import com.example.forum.application.DTOs.post.PostResponse;
import com.example.forum.application.services.CommentService;
import com.example.forum.application.services.PostService;
import com.example.forum.domain.model.comment.CommentId;
import com.example.forum.domain.model.post.PostId;
import com.example.forum.domain.model.user.UserId;
import com.example.forum.infrastructure.security.AuthUserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mod")
public class ModeratorController {

    private final PostService posts;
    private final CommentService comments;

    public ModeratorController(PostService posts, CommentService comments) {
        this.posts = posts;
        this.comments = comments;
    }


    @PostMapping("/posts/{id}/pin")
    public PostResponse pinPost(
            @PathVariable String id,
            @AuthenticationPrincipal AuthUserPrincipal principal
    ) {
        var post = posts.pinPost(new PostId(id), principal.getRole());
        return PostResponse.from(post, 0, false);
    }

    @PostMapping("/posts/{id}/lock")
    public PostResponse lockPost(
            @PathVariable String id,
            @AuthenticationPrincipal AuthUserPrincipal principal
    ) {
        var post = posts.lockPost(new PostId(id), principal.getRole());
        return PostResponse.from(post, 0, false);
    }

    @DeleteMapping("/posts/{id}")
    public void deletePost(
            @PathVariable String id,
            @AuthenticationPrincipal AuthUserPrincipal principal
    ) {
        posts.deletePost(
                new PostId(id),
                new UserId(principal.getUserId()),
                principal.getRole()
        );
    }


    @DeleteMapping("/comments/{id}")
    public void deleteComment(
            @PathVariable String id,
            @AuthenticationPrincipal AuthUserPrincipal principal
    ) {
        comments.deleteComment(
                new CommentId(id),
                new UserId(principal.getUserId()),
                principal.getRole()
        );
    }
}
