package com.backenddiploma.controllers;

import com.backenddiploma.services.MonobankService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/integrations/monobank")
@RequiredArgsConstructor
public class MonobankController {

    private final MonobankService monobankService;

    @PostMapping()
    public ResponseEntity<String> syncData(@RequestBody MonobankTokenDTO tokenDTO) {
        System.out.println("Received Monobank sync request for userId = " + tokenDTO.getUserId());

        monobankService.syncData(tokenDTO.getToken(), tokenDTO.getUserId());

        System.out.println("Monobank sync task enqueued for userId = " + tokenDTO.getUserId());
        return ResponseEntity.accepted().body("Monobank sync task enqueued for userId " + tokenDTO.getUserId());
    }
}

@Data
class MonobankTokenDTO {
    private String token;
    private Long userId;
}


