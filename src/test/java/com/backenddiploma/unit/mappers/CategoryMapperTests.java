package com.backenddiploma.unit.mappers;

import com.backenddiploma.dto.category.CategoryCreateDTO;
import com.backenddiploma.dto.category.CategoryResponseDTO;
import com.backenddiploma.dto.category.CategoryUpdateDTO;
import com.backenddiploma.dto.category.DefaultCategoryCreateDTO;
import com.backenddiploma.mappers.CategoryMapper;
import com.backenddiploma.models.Category;
import com.backenddiploma.models.User;
import com.backenddiploma.models.enums.BudgetType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryMapperTest {

    private CategoryMapper categoryMapper;

    @BeforeEach
    void setUp() {
        categoryMapper = new CategoryMapper();
    }

    @Test
    void testToEntity() {
        CategoryCreateDTO dto = new CategoryCreateDTO();
        dto.setName("Groceries");
        dto.setType(BudgetType.EXPENSE);
        dto.setDefault(false);
        dto.setIconUrl("http://icon.url/groceries.png");
        dto.setColor("#FF0000");

        User user = new User();
        user.setId(1L);

        Category category = categoryMapper.toEntity(dto, user);

        assertThat(category.getName()).isEqualTo("Groceries");
        assertThat(category.getType()).isEqualTo(BudgetType.EXPENSE);
        assertThat(category.isDefault()).isFalse();
        assertThat(category.getIconUrl()).isEqualTo("http://icon.url/groceries.png");
        assertThat(category.getColor()).isEqualTo("#FF0000");
        assertThat(category.getUser()).isEqualTo(user);
    }

    @Test
    void testDefaultToEntity() {
        DefaultCategoryCreateDTO dto = new DefaultCategoryCreateDTO();
        dto.setName("Utilities");
        dto.setType(BudgetType.EXPENSE);
        dto.setDefault(true);
        dto.setIconUrl("http://icon.url/utilities.png");
        dto.setNameUK("Комуналка");
        dto.setStartMcc(4900);
        dto.setEndMcc(4999);
        dto.setColor("#00FF00");

        User user = new User();
        user.setId(2L);

        Category category = categoryMapper.defaultToEntity(dto, user);

        assertThat(category.getName()).isEqualTo("Utilities");
        assertThat(category.getType()).isEqualTo(BudgetType.EXPENSE);
        assertThat(category.isDefault()).isTrue();
        assertThat(category.getIconUrl()).isEqualTo("http://icon.url/utilities.png");
        assertThat(category.getNameUK()).isEqualTo("Комуналка");
        assertThat(category.getStartMcc()).isEqualTo(4900);
        assertThat(category.getEndMcc()).isEqualTo(4999);
        assertThat(category.getColor()).isEqualTo("#00FF00");
        assertThat(category.getUser()).isEqualTo(user);
    }

    @Test
    void testUpdateCategoryFromDto() {
        Category category = new Category();
        category.setName("Old Name");
        category.setType(BudgetType.INCOME);
        category.setIconUrl("http://old.icon.url/icon.png");
        category.setColor("#123456");

        CategoryUpdateDTO dto = new CategoryUpdateDTO();
        dto.setName("New Name");
        dto.setType(BudgetType.EXPENSE);
        dto.setIconUrl("http://new.icon.url/new-icon.png");
        dto.setColor("#654321");

        categoryMapper.updateCategoryFromDto(category, dto);

        assertThat(category.getName()).isEqualTo("New Name");
        assertThat(category.getType()).isEqualTo(BudgetType.EXPENSE);
        assertThat(category.getIconUrl()).isEqualTo("http://new.icon.url/new-icon.png");
        assertThat(category.getColor()).isEqualTo("#654321");
    }

    @Test
    void testToResponse() {
        Category category = new Category();
        category.setId(5L);
        category.setName("Travel");
        category.setType(BudgetType.EXPENSE);
        category.setDefault(true);
        category.setIconUrl("http://icon.url/travel.png");
        category.setNameUK("Подорожі");
        category.setColor("#ABCDEF");

        User user = new User();
        user.setId(10L);
        category.setUser(user);

        category.setCreatedAt(LocalDateTime.of(2025, 6, 1, 12, 0));
        category.setUpdatedAt(LocalDateTime.of(2025, 6, 2, 15, 30));

        CategoryResponseDTO response = categoryMapper.toResponse(category);

        assertThat(response.getId()).isEqualTo(5L);
        assertThat(response.getName()).isEqualTo("Travel");
        assertThat(response.getType()).isEqualTo(BudgetType.EXPENSE);
        assertThat(response.isDefault()).isTrue();
        assertThat(response.getIconUrl()).isEqualTo("http://icon.url/travel.png");
        assertThat(response.getNameUK()).isEqualTo("Подорожі");
        assertThat(response.getColor()).isEqualTo("#ABCDEF");
        assertThat(response.getUserId()).isEqualTo(10L);
        assertThat(response.getCreatedAt()).isEqualTo(LocalDateTime.of(2025, 6, 1, 12, 0));
        assertThat(response.getUpdatedAt()).isEqualTo(LocalDateTime.of(2025, 6, 2, 15, 30));
    }
}
