package com.backenddiploma.controllers;

import com.backenddiploma.dto.transaction.TransactionCreateDTO;
import com.backenddiploma.dto.transaction.TransactionResponseDTO;
import com.backenddiploma.dto.transaction.TransactionUpdateDTO;
import com.backenddiploma.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getById(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        TransactionResponseDTO transaction = transactionService.getById(id, userId);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> create(@RequestBody @Valid TransactionCreateDTO dto) {
        TransactionResponseDTO created = transactionService.create(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid TransactionUpdateDTO dto,
            @RequestParam Long userId
    ) {
        TransactionResponseDTO updated = transactionService.update(id, dto, userId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        transactionService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }
}
