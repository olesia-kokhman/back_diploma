package com.backenddiploma.dto.paymentreminder;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PaymentReminderResponseDTO {

    private Long id;
    private Long userId;
    private String title;
    private double amount;
    private String currency;
    private LocalDate dueDate;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
