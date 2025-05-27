//package com.backenddiploma.security;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//import java.util.Date;
//
//@Component
//public class JwtCore {
//
//    @Value("${app.secret}")
//    private String secret;
//
//    @Value("${app.expiration}")
//    private int lifetime;
//
//    public String generateToken(Authentication authentication) {
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//        return Jwts.builder()
//                .setSubject(userDetails.getUsername())
//                .claim("role", userDetails.getRole().name())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + lifetime))
//                .signWith(SignatureAlgorithm.HS256, secret)
//                .compact();
//    }
//
//    public String getUsernameFromJwt(String token) {
//        return Jwts.parser()
//                .setSigningKey(secret)
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//
//    public boolean validateToken(String token, UserDetailsImpl userDetails) {
//        String username = getUsernameFromJwt(token);
//        return username.equals(userDetails.getUsername());
//    }
//
//
//}