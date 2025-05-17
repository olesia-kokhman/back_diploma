package com.backenddiploma.dto.category;

import com.backenddiploma.models.enums.BudgetType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryResponseDTO {

    private Long id;
    private String name;
    private BudgetType type;
    private boolean isDefault;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
