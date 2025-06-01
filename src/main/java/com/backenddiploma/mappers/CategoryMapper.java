package com.backenddiploma.mappers;

import com.backenddiploma.dto.category.CategoryCreateDTO;
import com.backenddiploma.dto.category.CategoryResponseDTO;
import com.backenddiploma.dto.category.CategoryUpdateDTO;
import com.backenddiploma.dto.category.DefaultCategoryCreateDTO;
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
        category.setIconUrl(dto.getIconUrl());
        category.setColor(dto.getColor());
        category.setUser(user);
        return category;
    }

    public Category defaultToEntity(DefaultCategoryCreateDTO dto, User user) {
        Category category = new Category();
        category.setName(dto.getName());
        category.setType(dto.getType());
        category.setDefault(dto.isDefault());
        category.setIconUrl(dto.getIconUrl());
        category.setNameUK(dto.getNameUK());
        category.setStartMcc(dto.getStartMcc());
        category.setEndMcc(dto.getEndMcc());
        category.setColor(dto.getColor());
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
        if (dto.getIconUrl() != null) {
            category.setIconUrl(dto.getIconUrl());
        }
        if(dto.getColor() != null) {
            category.setColor(dto.getColor());
        }
    }

    public CategoryResponseDTO toResponse(Category category) {
        CategoryResponseDTO response = new CategoryResponseDTO();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setType(category.getType());
        response.setDefault(category.isDefault());
        response.setIconUrl(category.getIconUrl());
        response.setNameUK(category.getNameUK());
        response.setColor(category.getColor());
        response.setUserId(category.getUser() != null ? category.getUser().getId() : null);
        response.setCreatedAt(category.getCreatedAt());
        response.setUpdatedAt(category.getUpdatedAt());
        return response;
    }
}
