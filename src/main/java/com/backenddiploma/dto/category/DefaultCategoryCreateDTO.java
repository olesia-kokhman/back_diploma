package com.backenddiploma.dto.category;

import com.backenddiploma.models.enums.BudgetType;
import lombok.Data;

@Data
public class DefaultCategoryCreateDTO {
    private String name;
    private String nameUK;
    private BudgetType type;
    private boolean isDefault;
    private Long userId;
    private String iconUrl;
    private String color;
    private Integer startMcc;
    private Integer endMcc;
}
