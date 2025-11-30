package com.example.forum.domain;

import com.example.forum.domain.model.user.*;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void shouldCreateUserWithHashedPassword() {

        Email email = new Email("test@mail.com");
        Username username = new Username("john");
        String hash = "asdlkfjasdfasdfasdf";

        User user = new User(
                UserId.generate(),
                email,
                username,
                hash,
                Role.USER,
                null,
                false,
                false,
                Instant.now(),
                Instant.now(),
                null
        );

        assertEquals(email, user.getEmail());
        assertEquals(username, user.getUsername());
        assertEquals(hash, user.getPasswordHash());
        assertEquals(Role.USER, user.getRole());
    }

    @Test
    void shouldRejectShortHash() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new User(
                        UserId.generate(),
                        new Email("a@mail.com"),
                        new Username("john"),
                        "123",
                        Role.USER,
                        null,
                        false,
                        false,
                        Instant.now(),
                        Instant.now(),
                        null
                )
        );
    }
}
