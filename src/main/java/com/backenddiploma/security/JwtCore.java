package com.backenddiploma.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtCore {
    @Value("${app.secret}")
    private String secret;

    @Value("${app.expiration}")
    private int lifetime;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(UserDetailsImpl userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + lifetime))
                .signWith(getSigningKey())
                .compact();
    }


    public String getUserEmailFromJwt(String token) {
        String subject = Jwts.parser().verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

        return subject;
    }


    public boolean validateToken(String token, UserDetailsImpl userDetails) {
        if (token == null || token.trim().isEmpty() || userDetails == null || userDetails.getId() == null) {
            return false;
        }

        try {
            String email = getUserEmailFromJwt(token);
            return email.equals(userDetails.getEmail());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}




