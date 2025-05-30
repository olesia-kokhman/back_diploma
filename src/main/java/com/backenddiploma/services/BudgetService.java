package com.backenddiploma.services;

import com.backenddiploma.dto.budget.BudgetCreateDTO;
import com.backenddiploma.dto.budget.BudgetResponseDTO;
import com.backenddiploma.dto.budget.BudgetUpdateDTO;
import com.backenddiploma.config.exceptions.NotFoundException;
import com.backenddiploma.mappers.BudgetMapper;
import com.backenddiploma.models.Budget;
import com.backenddiploma.models.Category;
import com.backenddiploma.models.User;
import com.backenddiploma.repositories.BudgetRepository;
import com.backenddiploma.repositories.CategoryRepository;
import com.backenddiploma.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final BudgetMapper budgetMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public BudgetResponseDTO create(BudgetCreateDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + dto.getUserId()));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + dto.getCategoryId()));

        Budget budget = budgetMapper.toEntity(dto, user, category);
        Budget savedBudget = budgetRepository.save(budget);

        return budgetMapper.toResponse(savedBudget);
    }

    @Transactional(readOnly = true)
    public BudgetResponseDTO getById(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Budget not found with id: " + id));
        return budgetMapper.toResponse(budget);
    }

    @Transactional
    public BudgetResponseDTO update(Long id, BudgetUpdateDTO dto) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Budget not found with id: " + id));

        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found with id: " + dto.getCategoryId()));
        }

        budgetMapper.updateBudgetFromDto(budget, dto, category);
        Budget updatedBudget = budgetRepository.save(budget);

        return budgetMapper.toResponse(updatedBudget);
    }

    @Transactional
    public void delete(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Budget not found with id: " + id));
        budgetRepository.delete(budget);
    }

    @Transactional(readOnly = true)
    public List<BudgetResponseDTO> getAllByUser(Long userId) {
        List<Budget> budgets = budgetRepository.findAllByUserId(userId);
        return budgets.stream().map(budgetMapper::toResponse).collect(Collectors.toList());
    }
}
