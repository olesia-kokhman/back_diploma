package com.backenddiploma.services;

import com.backenddiploma.config.exceptions.AlreadyExistsException;
import com.backenddiploma.dto.budget.BudgetCopyCreateDTO;
import com.backenddiploma.dto.budget.BudgetCreateDTO;
import com.backenddiploma.dto.budget.BudgetResponseDTO;
import com.backenddiploma.dto.budget.BudgetUpdateDTO;
import com.backenddiploma.config.exceptions.NotFoundException;
import com.backenddiploma.mappers.BudgetMapper;
import com.backenddiploma.models.Budget;
import com.backenddiploma.models.Category;
import com.backenddiploma.models.User;
import com.backenddiploma.models.enums.BudgetType;
import com.backenddiploma.models.enums.Currency;
import com.backenddiploma.repositories.BudgetRepository;
import com.backenddiploma.repositories.CategoryRepository;
import com.backenddiploma.repositories.TransactionRepository;
import com.backenddiploma.repositories.UserRepository;
import com.backenddiploma.services.charts.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final BudgetMapper budgetMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final ExchangeService exchangeService;
//    @Transactional
//    public BudgetResponseDTO create(BudgetCreateDTO dto) {
//        User user = userRepository.findById(dto.getUserId())
//                .orElseThrow(() -> new NotFoundException("User not found with id: " + dto.getUserId()));
//
//        Category category = categoryRepository.findById(dto.getCategoryId())
//                .orElseThrow(() -> new NotFoundException("Category not found with id: " + dto.getCategoryId()));
//
//        LocalDate monthStart = dto.getPeriodStart().withDayOfMonth(1);
//        boolean exists = budgetRepository.existsByUserIdAndCategoryIdAndPeriodStart(user.getId(), category.getId(), monthStart);
//
//        if (exists) {
//            throw new AlreadyExistsException("Budget already exists for this type/category/month");
//        }
//
//        Budget budget = budgetMapper.toEntity(dto, user, category);
//        budget.setPeriodStart(monthStart);
//        budget.setPeriodEnd(monthStart.withDayOfMonth(monthStart.lengthOfMonth()));
//        Budget savedBudget = budgetRepository.save(budget);
//        return budgetMapper.toResponse(savedBudget);
//    }

//    @Transactional
//    public BudgetResponseDTO create(BudgetCreateDTO dto) {
//        User user = userRepository.findById(dto.getUserId())
//                .orElseThrow(() -> new NotFoundException("User not found with id: " + dto.getUserId()));
//
//        Category category = categoryRepository.findById(dto.getCategoryId())
//                .orElseThrow(() -> new NotFoundException("Category not found with id: " + dto.getCategoryId()));
//
//        LocalDate monthStart = dto.getPeriodStart().withDayOfMonth(1);
//        boolean exists = budgetRepository.existsByUserIdAndCategoryIdAndPeriodStart(user.getId(), category.getId(), monthStart);
//
//        if (exists) {
//            throw new AlreadyExistsException("Budget already exists for this type/category/month");
//        }
//
//        // 🔸 Перевірка для EXPENSE (Total Expense)
//        if (category.getType().equals(BudgetType.EXPENSE)) {
//
//            Long uncategorizedExpenseCategoryId = 28L;
//
//            Budget totalExpenseBudget = budgetRepository
//                    .findByUserIdAndCategoryIdAndPeriodStart(user.getId(), uncategorizedExpenseCategoryId, monthStart)
//                    .orElseThrow(() -> new NotFoundException("Total expense budget (Uncategorized Expense) not found for this month"));
//
//            double totalExpensePlannedAmount = totalExpenseBudget.getPlannedAmount();
//
//            Double currentExpenseCategoriesSum = budgetRepository.sumPlannedAmountByUserIdAndPeriodStartAndExpenseCategoriesExcludingCategory(
//                    user.getId(), monthStart, uncategorizedExpenseCategoryId
//            );
//
//            if (currentExpenseCategoriesSum == null) {
//                currentExpenseCategoriesSum = 0.0;
//            }
//
//            double newTotal = currentExpenseCategoriesSum + dto.getPlannedAmount();
//
//            if (newTotal > totalExpensePlannedAmount) {
//                throw new AlreadyExistsException("Cannot add EXPENSE category budget. Total categories sum exceeds total expenses budget.");
//            }
//        }
//
//
//        if (category.getType().equals(BudgetType.INCOME)) {
//
//            Long uncategorizedIncomeCategoryId = 29L ; // ID системної категорії "Uncategorized Income"
//
//            Budget totalIncomeBudget = budgetRepository
//                    .findByUserIdAndCategoryIdAndPeriodStart(user.getId(), uncategorizedIncomeCategoryId, monthStart)
//                    .orElseThrow(() -> new NotFoundException("Total income budget (Uncategorized Income) not found for this month"));
//
//            double totalIncomePlannedAmount = totalIncomeBudget.getPlannedAmount();
//
//            Double currentIncomeCategoriesSum = budgetRepository.sumPlannedAmountByUserIdAndPeriodStartAndIncomeCategoriesExcludingCategory(
//                    user.getId(), monthStart, uncategorizedIncomeCategoryId
//            );
//
//            if (currentIncomeCategoriesSum == null) {
//                currentIncomeCategoriesSum = 0.0;
//            }
//
//            double newTotal = currentIncomeCategoriesSum + dto.getPlannedAmount();
//
//            if (newTotal > totalIncomePlannedAmount) {
//                throw new AlreadyExistsException("Cannot add INCOME category budget. Total categories sum exceeds total income budget.");
//            }
//        }
//
//        Budget budget = budgetMapper.toEntity(dto, user, category);
//        budget.setPeriodStart(monthStart);
//        budget.setPeriodEnd(monthStart.withDayOfMonth(monthStart.lengthOfMonth()));
//        Budget savedBudget = budgetRepository.save(budget);
//        return budgetMapper.toResponse(savedBudget);
//    }

    @Transactional
    public BudgetResponseDTO create(BudgetCreateDTO dto) {
        System.out.println("=== CREATE BUDGET START ===");
        System.out.println("UserId: " + dto.getUserId());
        System.out.println("CategoryId: " + dto.getCategoryId());
        System.out.println("PlannedAmount: " + dto.getPlannedAmount());
        System.out.println("Currency: " + dto.getCurrency());
        System.out.println("PeriodStart: " + dto.getPeriodStart());

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + dto.getUserId()));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + dto.getCategoryId()));

        LocalDate monthStart = dto.getPeriodStart().withDayOfMonth(1);

        System.out.println("Normalized monthStart: " + monthStart);

        boolean exists = budgetRepository.existsByUserIdAndCategoryIdAndPeriodStart(user.getId(), category.getId(), monthStart);

        if (exists) {
            System.out.println("ERROR: Budget already exists for this type/category/month!");
            throw new AlreadyExistsException("Budget already exists for this type/category/month");
        }

        if (category.getType().equals(BudgetType.EXPENSE)) {
            System.out.println("Category is EXPENSE");

            Long uncategorizedExpenseCategoryId = 28L;
            if (!category.getId().equals(uncategorizedExpenseCategoryId)) {
                System.out.println("Processing EXPENSE category (NOT Uncategorized Expense)");

                Budget totalExpenseBudget = budgetRepository
                        .findByUserIdAndCategoryIdAndPeriodStart(user.getId(), uncategorizedExpenseCategoryId, monthStart)
                        .orElseThrow(() -> new NotFoundException("Total expense budget (Uncategorized Expense) not found for this month"));

                Currency totalBudgetCurrency = totalExpenseBudget.getCurrency();
                double totalExpensePlannedAmount = totalExpenseBudget.getPlannedAmount();

                System.out.println("TotalExpenseBudget currency: " + totalBudgetCurrency);
                System.out.println("TotalExpenseBudget plannedAmount: " + totalExpensePlannedAmount);

                List<Budget> existingExpenseBudgets = budgetRepository
                        .findAllByUserIdAndPeriodStartAndExpenseCategoriesExcludingCategory(user.getId(), monthStart, uncategorizedExpenseCategoryId);

                System.out.println("Found " + existingExpenseBudgets.size() + " existing EXPENSE categories");

                double currentExpenseCategoriesSum = existingExpenseBudgets.stream()
                        .mapToDouble(budget -> {
                            Double converted = exchangeService.convert(
                                    budget.getPlannedAmount(),
                                    budget.getCurrency(),
                                    totalBudgetCurrency
                            );

                            if (converted == null) {
                                throw new RuntimeException("Currency conversion failed for categoryId=" + budget.getCategory().getId() +
                                        " from " + budget.getCurrency() + " to " + totalBudgetCurrency + ". Cannot proceed!");
                            }

                            System.out.println("Converted categoryId=" + budget.getCategory().getId() +
                                    " from " + budget.getCurrency() + " to " + totalBudgetCurrency + " -> " + converted);

                            return converted;
                        })
                        .sum();

                System.out.println("CurrentExpenseCategoriesSum: " + currentExpenseCategoriesSum);

                Double newCategoryAmount = exchangeService.convert(
                        dto.getPlannedAmount(),
                        dto.getCurrency(),
                        totalBudgetCurrency
                );

                if (newCategoryAmount == null) {
                    throw new RuntimeException("Currency conversion failed! Cannot add category budget because exchange rate not found from " + dto.getCurrency() + " to " + totalBudgetCurrency);
                }

                System.out.println("NewCategoryAmount converted to TotalCurrency: " + newCategoryAmount);

                double newTotal = currentExpenseCategoriesSum + newCategoryAmount;

                System.out.println("NewTotal after adding category: " + newTotal);

                if (newTotal > totalExpensePlannedAmount) {
                    System.out.println("ERROR: Total categories sum exceeds Total Expense Budget!");
                    throw new AlreadyExistsException("Cannot add EXPENSE category budget. Total categories sum exceeds total expenses budget.");
                }
            }
        }

        if (category.getType().equals(BudgetType.INCOME)) {
            System.out.println("Category is INCOME");

            Long uncategorizedIncomeCategoryId = 29L;
            if (!category.getId().equals(uncategorizedIncomeCategoryId)) {
                System.out.println("Processing INCOME category (NOT Uncategorized Income)");

                Budget totalIncomeBudget = budgetRepository
                        .findByUserIdAndCategoryIdAndPeriodStart(user.getId(), uncategorizedIncomeCategoryId, monthStart)
                        .orElseThrow(() -> new NotFoundException("Total income budget (Uncategorized Income) not found for this month"));

                Currency totalBudgetCurrency = totalIncomeBudget.getCurrency();
                double totalIncomePlannedAmount = totalIncomeBudget.getPlannedAmount();

                System.out.println("TotalIncomeBudget currency: " + totalBudgetCurrency);
                System.out.println("TotalIncomeBudget plannedAmount: " + totalIncomePlannedAmount);

                List<Budget> existingIncomeBudgets = budgetRepository
                        .findAllByUserIdAndPeriodStartAndIncomeCategoriesExcludingCategory(user.getId(), monthStart, uncategorizedIncomeCategoryId);

                System.out.println("Found " + existingIncomeBudgets.size() + " existing INCOME categories");

                double currentIncomeCategoriesSum = existingIncomeBudgets.stream()
                        .mapToDouble(budget -> {
                            Double converted = exchangeService.convert(
                                    budget.getPlannedAmount(),
                                    budget.getCurrency(),
                                    totalBudgetCurrency
                            );
                            System.out.println("Converted categoryId=" + budget.getCategory().getId() + " from " + budget.getCurrency() + " to " + totalBudgetCurrency + " -> " + converted);
                            return converted != null ? converted : 0.0;
                        })
                        .sum();

                System.out.println("CurrentIncomeCategoriesSum: " + currentIncomeCategoriesSum);

                Double newCategoryAmount = exchangeService.convert(
                        dto.getPlannedAmount(),
                        dto.getCurrency(),
                        totalBudgetCurrency
                );

                System.out.println("NewCategoryAmount converted to TotalCurrency: " + newCategoryAmount);

                double newTotal = currentIncomeCategoriesSum + (newCategoryAmount != null ? newCategoryAmount : 0.0);

                System.out.println("NewTotal after adding category: " + newTotal);

                if (newTotal > totalIncomePlannedAmount) {
                    System.out.println("ERROR: Total categories sum exceeds Total Income Budget!");
                    throw new AlreadyExistsException("Cannot add INCOME category budget. Total categories sum exceeds total income budget.");
                }
            }
        }

        System.out.println("Saving new budget...");

        Budget budget = budgetMapper.toEntity(dto, user, category);
        budget.setPeriodStart(monthStart);
        budget.setPeriodEnd(monthStart.withDayOfMonth(monthStart.lengthOfMonth()));
        Budget savedBudget = budgetRepository.save(budget);

        System.out.println("=== CREATE BUDGET SUCCESS === BudgetId: " + savedBudget.getId());

        return budgetMapper.toResponse(savedBudget);
    }


    @Transactional(readOnly = true)
    public BudgetResponseDTO getById(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Budget not found with id: " + id));
        return budgetMapper.toResponse(budget);
    }

    @Transactional(readOnly = true)
    public List<BudgetResponseDTO> getAllByUserAndPeriod(Long userId, LocalDate periodStart) {
        LocalDate monthStart = periodStart.withDayOfMonth(1);
        LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());

        List<Budget> budgets = budgetRepository.findAllByUserIdAndPeriodStart(userId, monthStart);

        return budgets.stream()
                .map(budget -> {
                    Double actualAmount = transactionRepository.calculateSpentAmount(
                            userId,
                            budget.getCategory().getId(),
                            monthStart.atStartOfDay(),
                            monthEnd.atTime(23, 59, 59)
                    );

                    double availableAmount = budget.getPlannedAmount() - actualAmount;

                    BudgetResponseDTO response = budgetMapper.toResponse(budget);
                    response.setActualAmount(actualAmount);
                    response.setAvailableAmount(availableAmount);
                    return response;
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public BudgetResponseDTO update(Long id, BudgetUpdateDTO dto) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Budget not found with id: " + id));

        Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found with id: " + dto.getCategoryId()));

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

    @Transactional
    public List<BudgetResponseDTO> copyBudgets(BudgetCopyCreateDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + dto.getUserId()));

        LocalDate fromMonthStart = dto.getFromPeriodStart().withDayOfMonth(1);
        LocalDate toMonthStart = dto.getToPeriodStart().withDayOfMonth(1);
        LocalDate toMonthEnd = toMonthStart.withDayOfMonth(toMonthStart.lengthOfMonth());

        List<Budget> sourceBudgets = budgetRepository.findAllByUserIdAndPeriodStart(user.getId(), fromMonthStart);
        boolean budgetsExist = budgetRepository.existsByUserIdAndPeriodStart(user.getId(), toMonthStart);

        if (budgetsExist) {
            throw new AlreadyExistsException("Budgets already exist for this month. Cannot copy.");
        }

        for (Budget sourceBudget : sourceBudgets) {
            Budget newBudget = new Budget();
            newBudget.setUser(user);
            newBudget.setCategory(sourceBudget.getCategory());
            newBudget.setPlannedAmount(sourceBudget.getPlannedAmount());
            newBudget.setCurrency(sourceBudget.getCurrency());
            newBudget.setPeriodStart(toMonthStart);
            newBudget.setPeriodEnd(toMonthEnd);
            budgetRepository.save(newBudget);
        }

        return getAllByUserAndPeriod(user.getId(), toMonthStart);
    }



}
