//package com.backenddiploma.security;
//
//import io.jsonwebtoken.ExpiredJwtException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//@RequiredArgsConstructor
//public class TokenFilter extends OncePerRequestFilter {
//    private final JwtCore jwtCore;
//    private final UserDetailsService userDetailsService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String jwt = null;
//        String username = null;
//        UserDetails userDetails = null;
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = null;
//
//        try {
//            String headerAuth = request.getHeader("Authorization");
//            if(headerAuth != null && headerAuth.startsWith("Bearer ")) {
//                jwt = headerAuth.substring(7);
//            }
//
//            if(jwt != null) {
//                try {
//                    username = jwtCore.getNameFromJwt(jwt);
//                } catch (ExpiredJwtException expiredJwtException) {
//
//                }
//
//                if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                    userDetails = userDetailsService.loadUserByUsername(username);
//                    usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null);
//                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//                }
//            }
//        } catch (Exception exception) {
//
//        }
//        filterChain.doFilter(request, response);
//    }
//}
