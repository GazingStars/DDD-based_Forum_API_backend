package com.example.forum.web.Controllers;

import com.example.forum.application.DTOs.avatar.AvatarResponse;
import com.example.forum.application.services.AuthService;
import com.example.forum.application.services.AvatarService;
import com.example.forum.application.services.UserService;
import com.example.forum.domain.model.user.User;
import com.example.forum.domain.model.user.UserId;
import com.example.forum.domain.repository.UserRepository;
import com.example.forum.infrastructure.security.AuthUserPrincipal;
import com.example.forum.application.DTOs.user.UpdateUserRequest;
import com.example.forum.web.DTOs.user.PublicUserResponse;
import com.example.forum.web.DTOs.user.UserResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserProfileController {

    private final UserRepository userRepository;
    private final UserService service;
    private final AvatarService avatarService;

    public UserProfileController(AuthService authService, UserRepository userRepository, UserService service, AvatarService avatarService) {
        this.userRepository = userRepository;
        this.service = service;
        this.avatarService = avatarService;
    }

    @GetMapping("/api/user/me")
    public UserResponse me(@AuthenticationPrincipal AuthUserPrincipal principal) {

        var user = userRepository.findById(
                new UserId(principal.getUserId())
        ).orElseThrow();

        return UserResponse.from((User) user);
    }

    @GetMapping("/api/user/{id}")
    public PublicUserResponse otherUser(@PathVariable String id) {
        var user = service.getById(new UserId(id));

        return PublicUserResponse.from((User) user);
    }

    @PutMapping("/api/user/me")
    public UserResponse change(@AuthenticationPrincipal AuthUserPrincipal principal, @RequestBody UpdateUserRequest request) {

        var changedUser = service.updateUser(new UserId(principal.getUserId()), request);

        return UserResponse.from(changedUser);
    }

    @GetMapping("/api/user/{id}/avatar")
    public AvatarResponse getUserAvatar(@PathVariable String id) {
        return avatarService.getForUser(new UserId(id))
                .map(AvatarResponse::from)
                .orElse(null);
    }
}
