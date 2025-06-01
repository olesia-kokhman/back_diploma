package com.backenddiploma.controllers;

import com.backenddiploma.services.integrations.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
public class CloudinaryController {
    private final CloudinaryService cloudinaryService;

    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestParam MultipartFile file) {

        String publicId = "default_category_icons/" + UUID.randomUUID();
        String imageUrl = cloudinaryService.uploadFile(file, publicId);
        return ResponseEntity.ok(imageUrl);
    }
}
