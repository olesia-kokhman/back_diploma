package com.backenddiploma.controllers;

import com.backenddiploma.dto.paymentreminder.PaymentReminderCreateDTO;
import com.backenddiploma.dto.paymentreminder.PaymentReminderResponseDTO;
import com.backenddiploma.dto.paymentreminder.PaymentReminderUpdateDTO;
import com.backenddiploma.services.PaymentReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-reminders")
@RequiredArgsConstructor
public class PaymentReminderController {

    private final PaymentReminderService paymentReminderService;

    @GetMapping("/nearest")
    public ResponseEntity<PaymentReminderResponseDTO> getNearest(@RequestParam Long userId) {
        return ResponseEntity.ok(paymentReminderService.getNearest(userId));
    }

    @PostMapping
    public ResponseEntity<PaymentReminderResponseDTO> create(@RequestBody PaymentReminderCreateDTO dto) {
        return ResponseEntity.ok(paymentReminderService.create(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PaymentReminderResponseDTO> update(@PathVariable Long id, @RequestBody PaymentReminderUpdateDTO dto) {
        return ResponseEntity.ok(paymentReminderService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentReminderResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentReminderService.getById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentReminderResponseDTO>> getAllByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(paymentReminderService.getByUserId(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        paymentReminderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
