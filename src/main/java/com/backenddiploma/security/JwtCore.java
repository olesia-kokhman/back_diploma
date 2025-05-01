package com.backenddiploma.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtCore {

    @Value("${app.secret}")
    private String secret;

    @Value("{app.expiration}")
    private int lifetime;

    public String generateToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder().subject((userDetails.getUsername())).issuedAt(new Date())
                .expiration(new Date((new Date().getTime() + lifetime)))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    };

    public String getNameFromJwt(String token) {
        return "jwt name"; // need to update
    }


}