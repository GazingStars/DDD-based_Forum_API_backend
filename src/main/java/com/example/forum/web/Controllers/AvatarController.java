package com.example.forum.web.Controllers;

import com.example.forum.application.services.AvatarService;
import com.example.forum.domain.model.user.UserId;
import com.example.forum.infrastructure.security.AuthUserPrincipal;
import com.example.forum.application.DTOs.avatar.AvatarResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user/me/avatar")
public class AvatarController {

    private final AvatarService service;

    public AvatarController(AvatarService service) {
        this.service = service;
    }

    @PostMapping(consumes = "multipart/form-data")
    public AvatarResponse uploadAvatar(
            @AuthenticationPrincipal AuthUserPrincipal principal,
            @RequestPart("file") MultipartFile file
    ) throws Exception {

        var avatar = service.upload(new UserId(principal.getUserId()), file);
        return AvatarResponse.from(avatar);
    }

    @DeleteMapping
    public void deleteAvatar(
            @AuthenticationPrincipal AuthUserPrincipal principal
    ) {
        service.delete(new UserId(principal.getUserId()));
    }
}
