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
        budget.setType(dto.getType());
        budget.setPlannedAmount(dto.getPlannedAmount());
        budget.setActualAmount(dto.getActualAmount());
        budget.setAvailableAmount(dto.getAvailableAmount());
        budget.setCurrency(Currency.valueOf(dto.getCurrency()));
        budget.setPeriodStart(dto.getPeriodStart());
        budget.setPeriodEnd(dto.getPeriodEnd());
        return budget;
    }

    public void updateBudgetFromDto(Budget budget, BudgetUpdateDTO dto, Category category) {
        if (category != null) {
            budget.setCategory(category);
        }
        if (dto.getType() != null) {
            budget.setType(dto.getType());
        }
        if (dto.getPlannedAmount() != null) {
            budget.setPlannedAmount(dto.getPlannedAmount());
        }
        if (dto.getActualAmount() != null) {
            budget.setActualAmount(dto.getActualAmount());
        }
        if (dto.getAvailableAmount() != null) {
            budget.setAvailableAmount(dto.getAvailableAmount());
        }
        if (dto.getCurrency() != null) {
            budget.setCurrency(Currency.valueOf(dto.getCurrency()));
        }
        if (dto.getPeriodStart() != null) {
            budget.setPeriodStart(dto.getPeriodStart());
        }
        if (dto.getPeriodEnd() != null) {
            budget.setPeriodEnd(dto.getPeriodEnd());
        }
    }

    public BudgetResponseDTO toResponse(Budget budget) {
        BudgetResponseDTO response = new BudgetResponseDTO();
        response.setId(budget.getId());
        response.setUserId(budget.getUser().getId());
        response.setCategoryId(budget.getCategory().getId());
        response.setType(budget.getType());
        response.setPlannedAmount(budget.getPlannedAmount());
        response.setActualAmount(budget.getActualAmount());
        response.setAvailableAmount(budget.getAvailableAmount());
        response.setCurrency(budget.getCurrency().toString());
        response.setPeriodStart(budget.getPeriodStart());
        response.setPeriodEnd(budget.getPeriodEnd());
        response.setCreatedAt(budget.getCreatedAt());
        response.setUpdatedAt(budget.getUpdatedAt());
        return response;
    }
}
