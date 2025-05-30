package com.backenddiploma.models;

import com.backenddiploma.models.enums.BudgetType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BudgetType type;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    @Column(name = "start_mcc")
    private int startMcc;

    @Column(name = "end_mcc")
    private int endMcc;

    @Column(name = "icon_url")
    private String iconUrl;

    @Column(name = "color")
    private String color;

    @Column(name = "name_uk")
    private String nameUK;

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

