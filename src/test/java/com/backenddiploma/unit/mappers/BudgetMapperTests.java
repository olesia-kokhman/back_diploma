package com.backenddiploma.unit.mappers;

import com.backenddiploma.dto.budget.BudgetCreateDTO;
import com.backenddiploma.dto.budget.BudgetResponseDTO;
import com.backenddiploma.dto.budget.BudgetUpdateDTO;
import com.backenddiploma.mappers.BudgetMapper;
import com.backenddiploma.models.Budget;
import com.backenddiploma.models.Category;
import com.backenddiploma.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.backenddiploma.models.enums.Currency.*;
import static org.assertj.core.api.Assertions.assertThat;

class BudgetMapperTest {

    private BudgetMapper budgetMapper;

    @BeforeEach
    void setUp() {
        budgetMapper = new BudgetMapper();
    }

    @Test
    void testToEntity() {
        BudgetCreateDTO dto = new BudgetCreateDTO();
        dto.setPlannedAmount(1500.0);
        dto.setCurrency(USD);
        dto.setPeriodStart(LocalDate.of(2025, 6, 1));

        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setId(2L);

        Budget budget = budgetMapper.toEntity(dto, user, category);

        assertThat(budget.getUser()).isEqualTo(user);
        assertThat(budget.getCategory()).isEqualTo(category);
        assertThat(budget.getPlannedAmount()).isEqualTo(1500.0);
        assertThat(budget.getCurrency()).isEqualTo(USD);
        assertThat(budget.getPeriodStart()).isEqualTo(LocalDate.of(2025, 6, 1));
    }

    @Test
    void testUpdateBudgetFromDto_withCategory() {
        Budget budget = new Budget();
        budget.setPlannedAmount(1000.0);
        budget.setCurrency(EUR);

        Category oldCategory = new Category();
        oldCategory.setId(1L);
        budget.setCategory(oldCategory);

        BudgetUpdateDTO dto = new BudgetUpdateDTO();
        dto.setPlannedAmount(2000.0);
        dto.setCurrency(USD);

        Category newCategory = new Category();
        newCategory.setId(2L);

        budgetMapper.updateBudgetFromDto(budget, dto, newCategory);

        assertThat(budget.getCategory()).isEqualTo(newCategory);
        assertThat(budget.getPlannedAmount()).isEqualTo(2000.0);
        assertThat(budget.getCurrency()).isEqualTo(USD);
    }

    @Test
    void testUpdateBudgetFromDto_withoutCategory() {
        Budget budget = new Budget();
        budget.setPlannedAmount(1000.0);
        budget.setCurrency(EUR);

        Category oldCategory = new Category();
        oldCategory.setId(1L);
        budget.setCategory(oldCategory);

        BudgetUpdateDTO dto = new BudgetUpdateDTO();
        dto.setPlannedAmount(2500.0);
        dto.setCurrency(UAH);

        // Pass null category — should not change the current category
        budgetMapper.updateBudgetFromDto(budget, dto, null);

        assertThat(budget.getCategory()).isEqualTo(oldCategory); // unchanged
        assertThat(budget.getPlannedAmount()).isEqualTo(2500.0);
        assertThat(budget.getCurrency()).isEqualTo(UAH);
    }

    @Test
    void testToResponse() {
        Budget budget = new Budget();
        budget.setId(5L);

        User user = new User();
        user.setId(10L);
        budget.setUser(user);

        Category category = new Category();
        category.setId(20L);
        budget.setCategory(category);

        budget.setPlannedAmount(3000.0);
        budget.setCurrency(USD);
        budget.setPeriodStart(LocalDate.of(2025, 5, 1));
        budget.setPeriodEnd(LocalDate.of(2025, 5, 31));

        budget.setCreatedAt(LocalDateTime.of(2025, 6, 1, 12, 0));
        budget.setUpdatedAt(LocalDateTime.of(2025, 6, 2, 15, 30));

        BudgetResponseDTO response = budgetMapper.toResponse(budget);

        assertThat(response.getId()).isEqualTo(5L);
        assertThat(response.getUserId()).isEqualTo(10L);
        assertThat(response.getCategoryId()).isEqualTo(20L);
        assertThat(response.getPlannedAmount()).isEqualTo(3000.0);
        assertThat(response.getCurrency()).isEqualTo(USD);
        assertThat(response.getPeriodStart()).isEqualTo(LocalDate.of(2025, 5, 1));
        assertThat(response.getPeriodEnd()).isEqualTo(LocalDate.of(2025, 5, 31));
        assertThat(response.getCreatedAt()).isEqualTo(LocalDateTime.of(2025, 6, 1, 12, 0));
        assertThat(response.getUpdatedAt()).isEqualTo(LocalDateTime.of(2025, 6, 2, 15, 30));
    }
}
