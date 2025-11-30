package com.example.forum.web.Controllers;

import com.example.forum.application.services.LikeService;
import com.example.forum.domain.model.comment.CommentId;
import com.example.forum.domain.model.post.PostId;
import com.example.forum.domain.model.user.UserId;
import com.example.forum.infrastructure.security.AuthUserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LikeController {

    private final LikeService service;

    public LikeController(LikeService service) {
        this.service = service;
    }

    @PostMapping("/posts/{postId}/like")
    public void likePost(
            @AuthenticationPrincipal AuthUserPrincipal principal,
            @PathVariable String postId
    ) {
        service.likePost(
                new UserId(principal.getUserId()),
                new PostId(postId)
        );
    }

    @DeleteMapping("/posts/{postId}/like")
    public void unlikePost(
            @AuthenticationPrincipal AuthUserPrincipal principal,
            @PathVariable String postId
    ) {
        service.unlikePost(
                new UserId(principal.getUserId()),
                new PostId(postId)
        );
    }

    @GetMapping("/posts/{postId}/likes")
    public long countPostLikes(@PathVariable String postId) {
        return service.countPostLikes(new PostId(postId));
    }

    @PostMapping("/comments/{commentId}/like")
    public void likeComment(
            @AuthenticationPrincipal AuthUserPrincipal principal,
            @PathVariable String commentId
    ) {
        service.likeComment(
                new UserId(principal.getUserId()),
                new CommentId(commentId)
        );
    }

    @DeleteMapping("/comments/{commentId}/like")
    public void unlikeComment(
            @AuthenticationPrincipal AuthUserPrincipal principal,
            @PathVariable String commentId
    ) {
        service.unlikeComment(
                new UserId(principal.getUserId()),
                new CommentId(commentId)
        );
    }

    @GetMapping("/comments/{commentId}/likes")
    public long countCommentLikes(@PathVariable String commentId) {
        return service.countCommentLikes(new CommentId(commentId));
    }
}
