package com.example.forum.application.DTOs.user;

import com.example.forum.domain.model.user.Role;

public record ChangeRoleRequest(
        Role role
) {}
