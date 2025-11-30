package com.example.forum.infrastructure.jpa.user;

import com.example.forum.domain.model.user.Role;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @Getter
    private String id;

    @Getter
    @Column(nullable = false, unique = true)
    private String email;

    @Getter
    @Column(nullable = false)
    private String username;

    @Getter
    @Column(nullable = false)
    private String passwordHash;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Getter
    @Column(nullable = false)
    private Boolean isBlocked;

    @Getter
    @Column(nullable = false)
    private Boolean isDeleted;

    @Getter
    @Column(name = "avatar_id")
    private String avatarId;

    @Getter
    @Column(nullable = false)
    private Instant createdAt;

    @Getter
    @Column(nullable = false)
    private Instant updatedAt;

    @Getter
    private Instant lastLoginAt;

    protected UserEntity() {
        // JPA
    }

    public UserEntity(
            String id,
            String email,
            String username,
            String passwordHash,
            Role role,
            Boolean isBlocked,
            Boolean isDeleted,
            String avatarId,
            Instant createdAt,
            Instant updatedAt,
            Instant lastLoginAt
    ) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.isBlocked = isBlocked;
        this.isDeleted = isDeleted;
        this.avatarId = avatarId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastLoginAt = lastLoginAt;
    }
}

