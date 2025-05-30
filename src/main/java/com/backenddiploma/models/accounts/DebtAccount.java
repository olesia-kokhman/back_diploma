package com.backenddiploma.models.accounts;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "debt_accounts")
@DiscriminatorValue("DEBT")
@Data
@NoArgsConstructor
public class DebtAccount extends Account {

    @Column(name = "lender_name", nullable = false)
    private String lenderName;

    @Column(name = "initial_amount", nullable = false)
    private double initialAmount;

    @Column(name = "current_amount", nullable = false)
    private double currentAmount;

    @Column(name = "interest_rate")
    private Double interestRate;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "is_recurring", nullable = false)
    private boolean isRecurring;
}
