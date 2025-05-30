package com.backenddiploma.dto.category;

import com.backenddiploma.models.enums.BudgetType;
import lombok.Data;

@Data
public class CategoryCreateDTO {

    private String name;
    private BudgetType type;
    private boolean isDefault;
    private Long userId;
    private String iconUrl;
    private String color;
}
