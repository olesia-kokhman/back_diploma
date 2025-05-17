package com.backenddiploma.mappers;

import com.backenddiploma.dto.category.CategoryCreateDTO;
import com.backenddiploma.dto.category.CategoryResponseDTO;
import com.backenddiploma.dto.category.CategoryUpdateDTO;
import com.backenddiploma.models.Category;
import com.backenddiploma.models.User;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryCreateDTO dto, User user) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setType(dto.getType());
        category.setDefault(dto.isDefault());
        category.setUser(user);
        return category;
    }

    public void updateCategoryFromDto(Category category, CategoryUpdateDTO dto) {
        if (dto.getName() != null) {
            category.setName(dto.getName());
        }
        if (dto.getType() != null) {
            category.setType(dto.getType());
        }
        if (dto.getIsDefault() != null) {
            category.setDefault(dto.getIsDefault());
        }
    }

    public CategoryResponseDTO toResponse(Category category) {
        CategoryResponseDTO response = new CategoryResponseDTO();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setType(category.getType());
        response.setDefault(category.isDefault());
        response.setUserId(category.getUser() != null ? category.getUser().getId() : null);
        response.setCreatedAt(category.getCreatedAt());
        response.setUpdatedAt(category.getUpdatedAt());
        return response;
    }
}
