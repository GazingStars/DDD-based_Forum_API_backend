package com.example.forum.domain.model.user;


import com.example.forum.domain.model.avatar.AvatarId;

import java.time.Instant;

public class User {

    private final UserId id;
    private Email email;
    private Username username;
    private String passwordHash;
    private Role role;

    private AvatarId avatarId;

    private boolean blocked;
    private boolean deleted;

    private final Instant createdAt;
    private Instant updatedAt;
    private Instant lastLoginAt;

    public User(UserId id,
                Email email,
                Username username,
                String passwordHash,
                Role role,
                AvatarId avatarId,
                boolean blocked,
                boolean deleted,
                Instant createdAt,
                Instant updatedAt,
                Instant lastLoginAt) {

        if (passwordHash == null || passwordHash.length() < 4) {
            throw new IllegalArgumentException("InvalidPasswordHash");
        }

        this.id = id;
        this.email = email;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;

        this.avatarId = avatarId;
        this.blocked = blocked;
        this.deleted = deleted;

        Instant now = Instant.now();
        this.createdAt = createdAt != null ? createdAt : now;
        this.updatedAt = updatedAt != null ? updatedAt : now;

        this.lastLoginAt = lastLoginAt;
    }

    public static User createNew(Email email,
                                 Username username,
                                 String passwordHash,
                                 AvatarId avatarId) {

        Instant now = Instant.now();

        return new User(
                UserId.generate(),
                email,
                username,
                passwordHash,
                Role.USER,
                avatarId,
                false,
                false,
                now,
                now,
                null
        );
    }


    private void touch() {
        this.updatedAt = Instant.now();
    }

    public void changeUsername(Username newUsername) {
        this.username = newUsername;
        touch();
    }

    public void changeEmail(Email newEmail) {
        this.email = newEmail;
        touch();
    }

    public void assignAvatar(AvatarId id) {
        this.avatarId = id;
        touch();
    }

    public void changePasswordHash(String newPasswordHash) {
        if (newPasswordHash.length() < 4) {
            throw new IllegalArgumentException("InvalidPasswordHash");
        }
        this.passwordHash = newPasswordHash;
        touch();
    }

    public void changeRole(Role newRole) {
        this.role = newRole;
        touch();
    }

    public void block() {
        this.blocked = true;
        touch();
    }

    public void unblock() {
        this.blocked = false;
        touch();
    }

    public void softDelete() {
        this.deleted = true;
        touch();
    }

    public void restore() {
        this.deleted = false;
        touch();
    }

    public void markLogin() {
        this.lastLoginAt = Instant.now();
        touch();
    }

    public UserId getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public Username getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public AvatarId getAvatarId() {
        return avatarId;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getLastLoginAt() {
        return lastLoginAt;
    }
}
