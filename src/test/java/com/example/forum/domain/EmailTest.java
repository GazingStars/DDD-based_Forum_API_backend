package com.example.forum.domain;

import com.example.forum.domain.model.user.Email;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmailTest {

    @Test
    void shouldThrowIfInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> new Email("not-email"));
    }

    @Test
    void shouldAcceptValidEmail() {
        Email email = new Email("test@mail.com");
        Assertions.assertEquals("test@mail.com", email.get());
    }
}
