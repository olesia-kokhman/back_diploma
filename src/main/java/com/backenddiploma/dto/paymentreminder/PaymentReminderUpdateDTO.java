package com.backenddiploma.dto.paymentreminder;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PaymentReminderUpdateDTO {

    private String title;
    private Double amount;
    private String currency;
    private LocalDate dueDate;
    private String description;
}
