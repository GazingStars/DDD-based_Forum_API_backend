package com.example.forum.web.Controllers;

import com.example.forum.application.DTOs.comment.CreateCommentRequest;
import com.example.forum.application.DTOs.comment.UpdateCommentRequest;
import com.example.forum.application.services.CommentService;
import com.example.forum.application.services.LikeService;
import com.example.forum.domain.model.comment.CommentId;
import com.example.forum.domain.model.post.PostId;
import com.example.forum.domain.model.user.UserId;
import com.example.forum.infrastructure.security.AuthUserPrincipal;
import com.example.forum.web.DTOs.comment.CommentResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    CommentService service;
    private final LikeService likeService;

    public CommentController(CommentService service, LikeService likeService) {
        this.service = service;
        this.likeService = likeService;
    }


    @PostMapping("/posts/{postId}/comments")
    public CommentResponse createComment(
            @AuthenticationPrincipal AuthUserPrincipal principal,
            @PathVariable String postId,
            @RequestBody CreateCommentRequest request
    ) {
        var comment = service.createComment(
                new PostId(postId),
                new UserId(principal.getUserId()),
                request
        );

        return CommentResponse.from(comment, 0, false);
    }

    @PutMapping("/comments/{id}")
    public CommentResponse edit(
            @AuthenticationPrincipal AuthUserPrincipal principal,
            @PathVariable String id,
            @RequestBody UpdateCommentRequest request
    ) {
        var comment = service.editComment(
                new CommentId(id),
                new UserId(principal.getUserId()),
                request
        );

        long likes = likeService.countCommentLikes(new CommentId(id));
        boolean likedByMe = likeService.hasUserLikedComment(
                new UserId(principal.getUserId()),
                new CommentId(id)
        );

        return CommentResponse.from(comment, likes, likedByMe);
    }

    @DeleteMapping("/comments/{id}")
    public void delete(
            @AuthenticationPrincipal AuthUserPrincipal principal,
            @PathVariable String id
    ) {
        service.deleteComment(
                new CommentId(id),
                new UserId(principal.getUserId()),
                principal.getRole()
        );
    }

    @GetMapping("/posts/{postId}/comments")
    public List<CommentResponse> list(
            @AuthenticationPrincipal AuthUserPrincipal principal,
            @PathVariable String postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        UserId currentUser = principal != null
                ? new UserId(principal.getUserId())
                : null;

        return service
                .getCommentsPaged(new PostId(postId), page, size)
                .stream()
                .map(c -> {
                    long likeCount = likeService.countCommentLikes(c.id());
                    boolean likedByMe = currentUser != null &&
                                        likeService.hasUserLikedComment(currentUser, c.id());

                    return CommentResponse.from(c, likeCount, likedByMe);
                })
                .toList();
    }

}
