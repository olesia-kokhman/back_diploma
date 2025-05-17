package com.backenddiploma.dto.category;

import com.backenddiploma.models.enums.BudgetType;
import lombok.Data;

@Data
public class CategoryUpdateDTO {

    private String name;
    private BudgetType type;
    private Boolean isDefault;
}
