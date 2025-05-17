package com.backenddiploma.dto.paymentreminder;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PaymentReminderCreateDTO {

    private Long userId;
    private String title;
    private double amount;
    private String currency;
    private LocalDate dueDate;
    private String description;
}
