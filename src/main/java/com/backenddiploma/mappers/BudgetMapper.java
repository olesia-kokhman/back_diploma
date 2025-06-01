package com.backenddiploma.mappers;

import com.backenddiploma.dto.budget.BudgetCreateDTO;
import com.backenddiploma.dto.budget.BudgetResponseDTO;
import com.backenddiploma.dto.budget.BudgetUpdateDTO;
import com.backenddiploma.models.Budget;
import com.backenddiploma.models.Category;
import com.backenddiploma.models.User;
import org.springframework.stereotype.Component;

import com.backenddiploma.models.enums.Currency;

@Component
public class BudgetMapper {

    public Budget toEntity(BudgetCreateDTO dto, User user, Category category) {
        Budget budget = new Budget();
        budget.setUser(user);
        budget.setCategory(category);
        budget.setPlannedAmount(dto.getPlannedAmount());
        budget.setCurrency(dto.getCurrency());
        budget.setPeriodStart(dto.getPeriodStart());
        return budget;
    }

    public void updateBudgetFromDto(Budget budget, BudgetUpdateDTO dto, Category category) {
        if (category != null) {
            budget.setCategory(category);
        }
        if (dto.getPlannedAmount() != null) {
            budget.setPlannedAmount(dto.getPlannedAmount());
        }
        if (dto.getCurrency() != null) {
            budget.setCurrency(dto.getCurrency());
        }
    }

    public BudgetResponseDTO toResponse(Budget budget) {
        BudgetResponseDTO response = new BudgetResponseDTO();
        response.setId(budget.getId());
        response.setUserId(budget.getUser().getId());
        response.setCategoryId(budget.getCategory().getId());
        response.setPlannedAmount(budget.getPlannedAmount());
        response.setCurrency(budget.getCurrency());
        response.setPeriodStart(budget.getPeriodStart());
        response.setPeriodEnd(budget.getPeriodEnd());
        response.setCreatedAt(budget.getCreatedAt());
        response.setUpdatedAt(budget.getUpdatedAt());
        return response;
    }
}
