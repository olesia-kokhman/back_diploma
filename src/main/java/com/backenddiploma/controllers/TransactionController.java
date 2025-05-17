package com.backenddiploma.controllers;

import com.backenddiploma.dto.transaction.TransactionCreateDTO;
import com.backenddiploma.dto.transaction.TransactionResponseDTO;
import com.backenddiploma.dto.transaction.TransactionUpdateDTO;
import com.backenddiploma.services.TransactionService;
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

    @GetMapping("/all")
    public ResponseEntity<Page<TransactionResponseDTO>> getTransactions(
            @RequestParam Long userId,
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(required = false) List<Long> accountIds,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "dateAndTime") String sortBy,
            @RequestParam(defaultValue = "true") boolean direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size
    ) {
        Page<TransactionResponseDTO> transactions = transactionService.getFilteredAndSortedTransactions(
                userId, categoryIds, accountIds, startDate, endDate, keyword, sortBy, direction, page, size);

        if (transactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getById(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        TransactionResponseDTO transaction = transactionService.getById(id, userId);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> create(
            @RequestBody TransactionCreateDTO dto,
            @RequestParam Long userId
    ) {
        TransactionResponseDTO created = transactionService.create(dto, userId);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> update(
            @PathVariable Long id,
            @RequestBody TransactionUpdateDTO dto,
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
