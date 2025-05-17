package com.backenddiploma.controllers;

import com.backenddiploma.dto.budget.BudgetCreateDTO;
import com.backenddiploma.dto.budget.BudgetResponseDTO;
import com.backenddiploma.dto.budget.BudgetUpdateDTO;
import com.backenddiploma.services.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<BudgetResponseDTO> create(@RequestBody BudgetCreateDTO dto) {
        return ResponseEntity.ok(budgetService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(budgetService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponseDTO> update(@PathVariable Long id, @RequestBody BudgetUpdateDTO dto) {
        return ResponseEntity.ok(budgetService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        budgetService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BudgetResponseDTO>> getAllByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(budgetService.getAllByUser(userId));
    }
}
