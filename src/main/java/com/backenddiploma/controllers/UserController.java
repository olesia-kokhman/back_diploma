package com.backenddiploma.controllers;

import com.backenddiploma.dto.user.UserCreateDTO;
import com.backenddiploma.dto.user.UserResponseDTO;
import com.backenddiploma.dto.user.UserUpdateDTO;
import com.backenddiploma.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody UserCreateDTO dto) {
        return ResponseEntity.ok(userService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id,
                                                  @ModelAttribute UserUpdateDTO dto) {
        return ResponseEntity.ok(userService.update(id, dto));
    }


    @DeleteMapping("/{id}/avatar")
    public ResponseEntity<Void> deleteAvatar(@PathVariable Long id) {
        userService.deleteAvatar(id);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }
}
