package com.backenddiploma.controllers;

import com.backenddiploma.dto.usersettings.UserSettingsCreateDTO;
import com.backenddiploma.dto.usersettings.UserSettingsResponseDTO;
import com.backenddiploma.dto.usersettings.UserSettingsUpdateDTO;
import com.backenddiploma.services.UserSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-settings")
@RequiredArgsConstructor
public class UserSettingsController {

    private final UserSettingsService userSettingsService;

    @PostMapping
    public ResponseEntity<UserSettingsResponseDTO> create(@RequestBody UserSettingsCreateDTO dto) {
        return ResponseEntity.ok(userSettingsService.create(dto));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserSettingsResponseDTO> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userSettingsService.getByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserSettingsResponseDTO> update(@PathVariable Long id, @RequestBody UserSettingsUpdateDTO dto) {
        return ResponseEntity.ok(userSettingsService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userSettingsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
