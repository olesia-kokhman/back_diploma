package com.backenddiploma.controllers;

import com.backenddiploma.dto.account.AccountCreateDTO;
import com.backenddiploma.dto.account.AccountResponseDTO;
import com.backenddiploma.dto.account.AccountUpdateDTO;
import com.backenddiploma.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponseDTO> create(@RequestBody AccountCreateDTO dto) {
        return ResponseEntity.ok(accountService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> update(@PathVariable Long id, @RequestBody AccountUpdateDTO dto) {
        return ResponseEntity.ok(accountService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountResponseDTO>> getAllByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(accountService.getAllByUser(userId));
    }
}
