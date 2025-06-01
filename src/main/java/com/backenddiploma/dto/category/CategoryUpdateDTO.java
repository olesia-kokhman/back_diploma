package com.backenddiploma.dto.category;

import com.backenddiploma.models.enums.BudgetType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CategoryUpdateDTO {

    private String name;
    private BudgetType type;
    private String iconUrl;
    private String color;
    private MultipartFile file;
}
