package com.backenddiploma.models;

import com.backenddiploma.models.enums.StockTrend;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_info")
@Data
@NoArgsConstructor
public class StockInfo {

    @Id
    @Column(length = 10, nullable = false)
    private String symbol;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "current_price", nullable = false)
    private double currentPrice;

    @Column(name = "change_percent", nullable = false)
    private double changePercent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StockTrend trend;

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
