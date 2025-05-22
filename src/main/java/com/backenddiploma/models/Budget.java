package com.backenddiploma.models;

import com.backenddiploma.models.enums.BudgetType;
import com.backenddiploma.models.enums.Currency;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "budgets")
@Data
@NoArgsConstructor
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @JoinColumn(name = "category_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BudgetType type;

    @Column(name = "planned_amount", nullable = false)
    private double plannedAmount;

    @Column(name = "actual_amount", nullable = false)
    private double actualAmount;

    @Column(name = "available_amount", nullable = false)
    private double availableAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
