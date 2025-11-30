package com.example.forum.infrastructure.security;

import com.example.forum.domain.model.user.Role;

public class AuthUserPrincipal {

    private final String userId;
    private final Role role;

    public AuthUserPrincipal(String userId, Role role) {
        this.userId = userId;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public Role getRole() {
        return role;
    }
}
