package com.backenddiploma.controllers;

import com.backenddiploma.models.UserSettings;
import com.backenddiploma.services.UserSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-settings")
@RequiredArgsConstructor
public class UserSettingsController {

    private final UserSettingsService userSettingsService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserSettings> getUserSettings(@PathVariable Long userId) {
        UserSettings settings = userSettingsService.getSettings(userId);
        if (settings != null) {
            return ResponseEntity.ok(settings);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{userId}")
    public ResponseEntity<UserSettings> createSettingsIfAbsent(@PathVariable Long userId) {
        UserSettings existing = userSettingsService.getSettings(userId);
        if (existing != null) {
            return ResponseEntity.status(409).body(existing);
        } else {
            UserSettings settings = userSettingsService.addSettingsIfAbsent(userId);
            return ResponseEntity.ok(settings);
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserSettings> updateUserSettings(@PathVariable Long userId, @RequestBody UserSettings updates) {
        try {
            UserSettings updatedSettings = userSettingsService.updateUserSettings(userId, updates);
            return ResponseEntity.ok(updatedSettings);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
