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
    public ResponseEntity<Void> syncData(@RequestBody MonobankTokenDTO tokenDTO) {
        System.out.println("this controller is working before service");
        monobankService.syncData(tokenDTO.getToken(), tokenDTO.getUserId());
        System.out.println("this controller is working after service");
        return ResponseEntity.ok().build();
    }
}

@Data
class MonobankTokenDTO {
    private String token;
    private Long userId;
}


