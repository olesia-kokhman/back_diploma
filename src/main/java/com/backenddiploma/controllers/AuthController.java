package com.backenddiploma.controllers;

import com.backenddiploma.dto.security.JwtResponseDTO;
import com.backenddiploma.dto.security.LoginRequestDTO;
import com.backenddiploma.dto.security.RegisterRequestDTO;
import com.backenddiploma.dto.security.UserMeDTO;
import com.backenddiploma.security.UserDetailsImpl;
import com.backenddiploma.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<JwtResponseDTO> register(@RequestBody RegisterRequestDTO request) {
        String token = authService.register(request);
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequestDTO request) {
        String token = authService.authenticate(request);
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }

    @GetMapping("/me")
    public ResponseEntity<UserMeDTO> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        UserMeDTO me = new UserMeDTO(
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail()
        );

        return ResponseEntity.ok(me);
    }
}

