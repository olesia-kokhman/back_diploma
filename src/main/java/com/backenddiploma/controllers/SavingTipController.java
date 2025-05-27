package com.backenddiploma.controllers;

import com.backenddiploma.dto.savingtip.SavingTipCreateDTO;
import com.backenddiploma.dto.savingtip.SavingTipResponseDTO;
import com.backenddiploma.dto.savingtip.SavingTipUpdateDTO;
import com.backenddiploma.services.SavingTipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saving-tips")
@RequiredArgsConstructor
public class SavingTipController {

    private final SavingTipService savingTipService;

    @GetMapping("/random")
    public ResponseEntity<SavingTipResponseDTO> getRandom() {
        return ResponseEntity.ok(savingTipService.getRandomTip());
    }

    @PostMapping
    public ResponseEntity<SavingTipResponseDTO> create(@RequestBody SavingTipCreateDTO dto) {
        return ResponseEntity.ok(savingTipService.create(dto));
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<SavingTipResponseDTO> getById(@PathVariable Long id) {
//        return ResponseEntity.ok(savingTipService.getById(id));
//    }

    @PutMapping("/{id}")
    public ResponseEntity<SavingTipResponseDTO> update(@PathVariable Long id, @RequestBody SavingTipUpdateDTO dto) {
        return ResponseEntity.ok(savingTipService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        savingTipService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SavingTipResponseDTO>> getAll() {
        return ResponseEntity.ok(savingTipService.getAll());
    }
}
