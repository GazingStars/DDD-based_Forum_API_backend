package com.example.forum.infrastructure.security;

import com.example.forum.domain.model.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Controller;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Controller
public class JwtProvider {
    private final JwtProperties properties;
    private final Key key;

    public JwtProvider(JwtProperties properties) {
        this.properties = properties;
        this.key = Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId().get())      // UserId
                .claim("role", user.getRole().name()) // роль
                .setExpiration(new Date(System.currentTimeMillis() + properties.getExpiration()))
                .signWith(key)
                .compact();
    }

    public Jws<Claims> validate(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

}
