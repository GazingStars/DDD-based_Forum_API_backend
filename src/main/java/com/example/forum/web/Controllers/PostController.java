package com.example.forum.web.Controllers;

import com.example.forum.application.DTOs.post.CreatePostRequest;
import com.example.forum.application.DTOs.post.PostResponse;
import com.example.forum.application.DTOs.post.UpdatePostRequest;
import com.example.forum.application.services.LikeService;
import com.example.forum.application.services.PostService;
import com.example.forum.domain.model.like.LikeTargetId;
import com.example.forum.domain.model.like.LikeTargetType;
import com.example.forum.domain.model.post.PostId;
import com.example.forum.domain.model.user.UserId;
import com.example.forum.infrastructure.security.AuthUserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService service;
    private final LikeService likeService;

    public PostController(PostService service, LikeService likeService) {
        this.service = service;
        this.likeService = likeService;
    }

    @GetMapping
    public List<PostResponse> list(
            @AuthenticationPrincipal AuthUserPrincipal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        var posts = service.getPaged(page, size);

        return posts.stream()
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


    @GetMapping("/{id}")
    public PostResponse get(
            @AuthenticationPrincipal AuthUserPrincipal principal,
            @PathVariable String id
    ) {
        var post = service.getPost(new PostId(id));

        long likes = likeService.countPostLikes(new PostId(id));

        boolean likedByMe = false;
        if (principal != null) {
            likedByMe = likeService.hasUserLikedPost(
                    new UserId(principal.getUserId()),
                    new PostId(id)
            );
        }

        return PostResponse.from(post, likes, likedByMe);
    }

    @GetMapping("/user/{userId}")
    public List<PostResponse> getByUser(
            @AuthenticationPrincipal AuthUserPrincipal principal,
            @PathVariable String userId
    ) {
        var posts = service.getByUser(new UserId(userId));

        return posts.stream()
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

    @PostMapping
    public PostResponse create(
            @AuthenticationPrincipal AuthUserPrincipal principal,
            @RequestBody CreatePostRequest dto
    ) {
        var post = service.createPost(new UserId(principal.getUserId()), dto);

        // новый пост → лайков нет
        return PostResponse.from(post, 0, false);
    }

    @PutMapping("/{id}")
    public PostResponse edit(
            @AuthenticationPrincipal AuthUserPrincipal principal,
            @PathVariable String id,
            @RequestBody UpdatePostRequest dto
    ) {
        var post = service.editPost(
                new PostId(id),
                new UserId(principal.getUserId()),
                principal.getRole(),
                dto
        );

        long likeCount = likeService.countPostLikes(new PostId(id));

        boolean likedByMe = likeService.hasUserLikedPost(
                new UserId(principal.getUserId()),
                new PostId(id)
        );

        return PostResponse.from(post, likeCount, likedByMe);
    }


    @GetMapping("/category/{slug}")
    public List<PostResponse> listByCategory(
            @AuthenticationPrincipal AuthUserPrincipal principal,
            @PathVariable String slug,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        var posts = service.getByCategorySlug(slug, page, size);

        return posts.stream()
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

    @DeleteMapping("/{id}")
    public void delete(
            @AuthenticationPrincipal AuthUserPrincipal principal,
            @PathVariable String id
    ) {
        service.deletePost(
                new PostId(id),
                new UserId(principal.getUserId()),
                principal.getRole()
        );
    }

}
