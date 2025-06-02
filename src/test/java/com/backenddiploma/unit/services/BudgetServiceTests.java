package com.backenddiploma.unit.services;
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
import com.backenddiploma.services.BudgetService;
import com.backenddiploma.services.CategoryService;
import com.backenddiploma.services.charts.ExchangeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {

    @InjectMocks
    private BudgetService budgetService;

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private BudgetMapper budgetMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ExchangeService exchangeService;

    @Mock
    private CategoryService categoryService;

    // === create ===

    @Test
    void whenCreateBudget_thenSaveAndReturn() {
        BudgetCreateDTO dto = new BudgetCreateDTO();
        dto.setUserId(1L);
        dto.setCategoryId(2L);
        dto.setPeriodStart(LocalDate.of(2024, 6, 1));
        dto.setPlannedAmount(100.0);
        dto.setCurrency(Currency.USD);

        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setId(2L);
        category.setType(BudgetType.EXPENSE);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(budgetRepository.existsByUserIdAndCategoryIdAndPeriodStart(1L, 2L, LocalDate.of(2024, 6, 1))).thenReturn(false);
        when(categoryService.getCategoryId("Uncategorized", 1L, BudgetType.EXPENSE)).thenReturn(28L);
        when(budgetRepository.findByUserIdAndCategoryIdAndPeriodStart(1L, 28L, LocalDate.of(2024, 6, 1)))
                .thenReturn(Optional.of(createTotalExpenseBudget()));

        when(budgetRepository.findAllByUserIdAndPeriodStartAndExpenseCategoriesExcludingCategory(1L, LocalDate.of(2024, 6, 1), 28L))
                .thenReturn(List.of());

        when(exchangeService.convert(100.0, Currency.USD, Currency.UAH)).thenReturn(4000.0);

        when(budgetMapper.toEntity(dto, user, category)).thenReturn(new Budget());
        when(budgetRepository.save(any())).thenReturn(new Budget());
        when(budgetMapper.toResponse(any())).thenReturn(new BudgetResponseDTO());

        BudgetResponseDTO result = budgetService.create(dto);

        assertNotNull(result);
        verify(budgetRepository).save(any());
    }

    @Test
    void whenCreateBudget_budgetExists_thenThrow() {
        BudgetCreateDTO dto = new BudgetCreateDTO();
        dto.setUserId(1L);
        dto.setCategoryId(2L);
        dto.setPeriodStart(LocalDate.of(2024, 6, 1));

        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(budgetRepository.existsByUserIdAndCategoryIdAndPeriodStart(1L, 2L, LocalDate.of(2024, 6, 1))).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> budgetService.create(dto));
    }

    @Test
    void whenCreateBudget_userNotFound_thenThrow() {
        BudgetCreateDTO dto = new BudgetCreateDTO();
        dto.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> budgetService.create(dto));
    }

    @Test
    void whenCreateBudget_categoryNotFound_thenThrow() {
        BudgetCreateDTO dto = new BudgetCreateDTO();
        dto.setUserId(1L);
        dto.setCategoryId(2L);

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> budgetService.create(dto));
    }

    // === getById ===

    @Test
    void whenGetById_thenReturnBudget() {
        Budget budget = new Budget();

        when(budgetRepository.findById(1L)).thenReturn(Optional.of(budget));
        when(budgetMapper.toResponse(budget)).thenReturn(new BudgetResponseDTO());

        BudgetResponseDTO result = budgetService.getById(1L);

        assertNotNull(result);
    }

    @Test
    void whenGetById_notFound_thenThrow() {
        when(budgetRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> budgetService.getById(1L));
    }

    // === getAllByUserAndPeriod ===

    @Test
    void whenGetAllByUserAndPeriod_thenReturnList() {
        Budget budget = new Budget();
        budget.setPlannedAmount(100.0);
        Category category = new Category();
        category.setId(2L);
        budget.setCategory(category);

        when(budgetRepository.findAllByUserIdAndPeriodStart(1L, LocalDate.of(2024, 6, 1))).thenReturn(List.of(budget));
        when(transactionRepository.calculateSpentAmount(1L, 2L, LocalDate.of(2024, 6, 1).atStartOfDay(), LocalDate.of(2024, 6, 30).atTime(23, 59, 59)))
                .thenReturn(50.0);
        when(budgetMapper.toResponse(budget)).thenReturn(new BudgetResponseDTO());

        List<BudgetResponseDTO> result = budgetService.getAllByUserAndPeriod(1L, LocalDate.of(2024, 6, 15));

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    // === update ===

    @Test
    void whenUpdateBudget_thenSaveAndReturn() {
        Budget budget = new Budget();

        Category category = new Category();
        category.setId(2L);

        BudgetUpdateDTO dto = new BudgetUpdateDTO();
        dto.setCategoryId(2L);

        when(budgetRepository.findById(1L)).thenReturn(Optional.of(budget));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(budgetRepository.save(budget)).thenReturn(budget);
        when(budgetMapper.toResponse(budget)).thenReturn(new BudgetResponseDTO());

        BudgetResponseDTO result = budgetService.update(1L, dto);

        assertNotNull(result);
        verify(budgetRepository).save(budget);
    }

    @Test
    void whenCreateExpenseBudget_totalExpenseBudgetNotFound_thenThrow() {
        BudgetCreateDTO dto = new BudgetCreateDTO();
        dto.setUserId(1L);
        dto.setCategoryId(2L);
        dto.setPeriodStart(LocalDate.of(2024, 6, 1));
        dto.setPlannedAmount(100.0);
        dto.setCurrency(Currency.USD);

        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setId(2L);
        category.setType(BudgetType.EXPENSE);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(budgetRepository.existsByUserIdAndCategoryIdAndPeriodStart(1L, 2L, LocalDate.of(2024, 6, 1))).thenReturn(false);
        when(categoryService.getCategoryId("Uncategorized", 1L, BudgetType.EXPENSE)).thenReturn(28L);
        when(budgetRepository.findByUserIdAndCategoryIdAndPeriodStart(1L, 28L, LocalDate.of(2024, 6, 1)))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> budgetService.create(dto));
    }

    @Test
    void whenCreateExpenseBudget_conversionFails_thenThrow() {
        BudgetCreateDTO dto = new BudgetCreateDTO();
        dto.setUserId(1L);
        dto.setCategoryId(2L);
        dto.setPeriodStart(LocalDate.of(2024, 6, 1));
        dto.setPlannedAmount(100.0);
        dto.setCurrency(Currency.USD);

        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setId(2L);
        category.setType(BudgetType.EXPENSE);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(budgetRepository.existsByUserIdAndCategoryIdAndPeriodStart(1L, 2L, LocalDate.of(2024, 6, 1))).thenReturn(false);
        when(categoryService.getCategoryId("Uncategorized", 1L, BudgetType.EXPENSE)).thenReturn(28L);
        when(budgetRepository.findByUserIdAndCategoryIdAndPeriodStart(1L, 28L, LocalDate.of(2024, 6, 1)))
                .thenReturn(Optional.of(createTotalExpenseBudget()));

        when(budgetRepository.findAllByUserIdAndPeriodStartAndExpenseCategoriesExcludingCategory(1L, LocalDate.of(2024, 6, 1), 28L))
                .thenReturn(List.of());

        // conversion fails:
        when(exchangeService.convert(100.0, Currency.USD, Currency.UAH)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> budgetService.create(dto));
    }

    @Test
    void whenCreateExpenseBudget_existingBudgetsConversionFails_thenThrow() {
        BudgetCreateDTO dto = new BudgetCreateDTO();
        dto.setUserId(1L);
        dto.setCategoryId(2L);
        dto.setPeriodStart(LocalDate.of(2024, 6, 1));
        dto.setPlannedAmount(100.0);
        dto.setCurrency(Currency.USD);

        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setId(2L);
        category.setType(BudgetType.EXPENSE);

        Budget existingBudget = new Budget();
        existingBudget.setPlannedAmount(200.0);
        existingBudget.setCurrency(Currency.EUR);
        existingBudget.setCategory(new Category());
        existingBudget.getCategory().setId(99L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(budgetRepository.existsByUserIdAndCategoryIdAndPeriodStart(1L, 2L, LocalDate.of(2024, 6, 1))).thenReturn(false);
        when(categoryService.getCategoryId("Uncategorized", 1L, BudgetType.EXPENSE)).thenReturn(28L);
        when(budgetRepository.findByUserIdAndCategoryIdAndPeriodStart(1L, 28L, LocalDate.of(2024, 6, 1)))
                .thenReturn(Optional.of(createTotalExpenseBudget()));

        when(budgetRepository.findAllByUserIdAndPeriodStartAndExpenseCategoriesExcludingCategory(1L, LocalDate.of(2024, 6, 1), 28L))
                .thenReturn(List.of(existingBudget));

        // existing conversion fails:
        when(exchangeService.convert(200.0, Currency.EUR, Currency.UAH)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> budgetService.create(dto));
    }

    @Test
    void whenCreateExpenseBudget_sumExceedsTotal_thenThrow() {
        BudgetCreateDTO dto = new BudgetCreateDTO();
        dto.setUserId(1L);
        dto.setCategoryId(2L);
        dto.setPeriodStart(LocalDate.of(2024, 6, 1));
        dto.setPlannedAmount(100.0);
        dto.setCurrency(Currency.USD);

        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setId(2L);
        category.setType(BudgetType.EXPENSE);

        Budget existingBudget = new Budget();
        existingBudget.setPlannedAmount(4900.0);
        existingBudget.setCurrency(Currency.UAH);
        existingBudget.setCategory(new Category());
        existingBudget.getCategory().setId(99L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(budgetRepository.existsByUserIdAndCategoryIdAndPeriodStart(1L, 2L, LocalDate.of(2024, 6, 1))).thenReturn(false);
        when(categoryService.getCategoryId("Uncategorized", 1L, BudgetType.EXPENSE)).thenReturn(28L);
        when(budgetRepository.findByUserIdAndCategoryIdAndPeriodStart(1L, 28L, LocalDate.of(2024, 6, 1)))
                .thenReturn(Optional.of(createTotalExpenseBudget()));

        when(budgetRepository.findAllByUserIdAndPeriodStartAndExpenseCategoriesExcludingCategory(1L, LocalDate.of(2024, 6, 1), 28L))
                .thenReturn(List.of(existingBudget));

        when(exchangeService.convert(4900.0, Currency.UAH, Currency.UAH)).thenReturn(4900.0);
        when(exchangeService.convert(100.0, Currency.USD, Currency.UAH)).thenReturn(200.0);

        // new total = 4900 + 200 = 5100 > 5000 → exception
        assertThrows(AlreadyExistsException.class, () -> budgetService.create(dto));
    }

    @Test
    void whenCreateExpenseBudget_Uncategorized_thenSaveWithoutOverflowCheck() {
        BudgetCreateDTO dto = new BudgetCreateDTO();
        dto.setUserId(1L);
        dto.setCategoryId(28L); // UNCATEGORIZED
        dto.setPeriodStart(LocalDate.of(2024, 6, 1));
        dto.setPlannedAmount(100.0);
        dto.setCurrency(Currency.USD);

        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setId(28L);
        category.setType(BudgetType.EXPENSE);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(28L)).thenReturn(Optional.of(category));
        when(budgetRepository.existsByUserIdAndCategoryIdAndPeriodStart(1L, 28L, LocalDate.of(2024, 6, 1))).thenReturn(false);

        when(budgetMapper.toEntity(dto, user, category)).thenReturn(new Budget());
        when(budgetRepository.save(any())).thenReturn(new Budget());
        when(budgetMapper.toResponse(any())).thenReturn(new BudgetResponseDTO());

        BudgetResponseDTO result = budgetService.create(dto);

        assertNotNull(result);
        verify(budgetRepository).save(any());
    }

    private Budget createTotalIncomeBudget() {
        Budget b = new Budget();
        b.setCurrency(Currency.UAH);
        b.setPlannedAmount(5000.0);
        return b;
    }


    @Test
    void whenUpdateBudget_notFound_thenThrow() {
        when(budgetRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> budgetService.update(1L, new BudgetUpdateDTO()));
    }

    @Test
    void whenUpdateBudget_categoryNotFound_thenThrow() {
        Budget budget = new Budget();

        BudgetUpdateDTO dto = new BudgetUpdateDTO();
        dto.setCategoryId(2L);

        when(budgetRepository.findById(1L)).thenReturn(Optional.of(budget));
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> budgetService.update(1L, dto));
    }

    // === delete ===

    @Test
    void whenDeleteBudget_thenDelete() {
        Budget budget = new Budget();

        when(budgetRepository.findById(1L)).thenReturn(Optional.of(budget));

        budgetService.delete(1L);

        verify(budgetRepository).delete(budget);
    }

    @Test
    void whenDeleteBudget_notFound_thenThrow() {
        when(budgetRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> budgetService.delete(1L));
    }

    // === copyBudgets ===

    @Test
    void whenCopyBudgets_thenCopyAndReturn() {
        BudgetCopyCreateDTO dto = new BudgetCopyCreateDTO();
        dto.setUserId(1L);
        dto.setFromPeriodStart(LocalDate.of(2024, 5, 1));
        dto.setToPeriodStart(LocalDate.of(2024, 6, 1));

        User user = new User();
        user.setId(1L);

        Budget sourceBudget = new Budget();
        sourceBudget.setCategory(new Category());
        sourceBudget.setPlannedAmount(100.0);
        sourceBudget.setCurrency(Currency.USD);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(budgetRepository.findAllByUserIdAndPeriodStart(1L, LocalDate.of(2024, 5, 1))).thenReturn(List.of(sourceBudget));
        when(budgetRepository.existsByUserIdAndPeriodStart(1L, LocalDate.of(2024, 6, 1))).thenReturn(false);
        when(budgetRepository.save(any())).thenReturn(new Budget());
        when(budgetRepository.findAllByUserIdAndPeriodStart(1L, LocalDate.of(2024, 6, 1))).thenReturn(List.of());

        List<BudgetResponseDTO> result = budgetService.copyBudgets(dto);

        assertNotNull(result);
    }
    @Test
    void whenCreateIncomeBudget_thenSaveAndReturn() {
        BudgetCreateDTO dto = new BudgetCreateDTO();
        dto.setUserId(1L);
        dto.setCategoryId(2L);
        dto.setPeriodStart(LocalDate.of(2024, 6, 1));
        dto.setPlannedAmount(100.0);
        dto.setCurrency(Currency.USD);

        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setId(2L);
        category.setType(BudgetType.INCOME);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(budgetRepository.existsByUserIdAndCategoryIdAndPeriodStart(1L, 2L, LocalDate.of(2024, 6, 1))).thenReturn(false);
        when(categoryService.getCategoryId("Uncategorized", 1L, BudgetType.INCOME)).thenReturn(29L);
        when(budgetRepository.findByUserIdAndCategoryIdAndPeriodStart(1L, 29L, LocalDate.of(2024, 6, 1)))
                .thenReturn(Optional.of(createTotalIncomeBudget()));

        when(budgetRepository.findAllByUserIdAndPeriodStartAndIncomeCategoriesExcludingCategory(1L, LocalDate.of(2024, 6, 1), 29L))
                .thenReturn(List.of());

        when(exchangeService.convert(100.0, Currency.USD, Currency.UAH)).thenReturn(4000.0);

        when(budgetMapper.toEntity(dto, user, category)).thenReturn(new Budget());
        when(budgetRepository.save(any())).thenReturn(new Budget());
        when(budgetMapper.toResponse(any())).thenReturn(new BudgetResponseDTO());

        BudgetResponseDTO result = budgetService.create(dto);

        assertNotNull(result);
        verify(budgetRepository).save(any());
    }

    @Test
    void whenCreateIncomeBudget_totalIncomeBudgetNotFound_thenThrow() {
        BudgetCreateDTO dto = new BudgetCreateDTO();
        dto.setUserId(1L);
        dto.setCategoryId(2L);
        dto.setPeriodStart(LocalDate.of(2024, 6, 1));
        dto.setPlannedAmount(100.0);
        dto.setCurrency(Currency.USD);

        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setId(2L);
        category.setType(BudgetType.INCOME);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(budgetRepository.existsByUserIdAndCategoryIdAndPeriodStart(1L, 2L, LocalDate.of(2024, 6, 1))).thenReturn(false);
        when(categoryService.getCategoryId("Uncategorized", 1L, BudgetType.INCOME)).thenReturn(29L);
        when(budgetRepository.findByUserIdAndCategoryIdAndPeriodStart(1L, 29L, LocalDate.of(2024, 6, 1)))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> budgetService.create(dto));
    }

    @Test
    void whenCreateIncomeBudget_conversionFails_thenThrow() {
        BudgetCreateDTO dto = new BudgetCreateDTO();
        dto.setUserId(1L);
        dto.setCategoryId(2L);
        dto.setPeriodStart(LocalDate.of(2024, 6, 1));
        dto.setPlannedAmount(100.0);
        dto.setCurrency(Currency.USD);

        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setId(2L);
        category.setType(BudgetType.INCOME);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(budgetRepository.existsByUserIdAndCategoryIdAndPeriodStart(1L, 2L, LocalDate.of(2024, 6, 1))).thenReturn(false);
        when(categoryService.getCategoryId("Uncategorized", 1L, BudgetType.INCOME)).thenReturn(29L);
        when(budgetRepository.findByUserIdAndCategoryIdAndPeriodStart(1L, 29L, LocalDate.of(2024, 6, 1)))
                .thenReturn(Optional.of(createTotalIncomeBudget()));

        when(budgetRepository.findAllByUserIdAndPeriodStartAndIncomeCategoriesExcludingCategory(1L, LocalDate.of(2024, 6, 1), 29L))
                .thenReturn(List.of());

        when(exchangeService.convert(100.0, Currency.USD, Currency.UAH)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> budgetService.create(dto));
    }

    @Test
    void whenCreateIncomeBudget_existingBudgetsConversionFails_thenThrow() {
        BudgetCreateDTO dto = new BudgetCreateDTO();
        dto.setUserId(1L);
        dto.setCategoryId(2L);
        dto.setPeriodStart(LocalDate.of(2024, 6, 1));
        dto.setPlannedAmount(100.0);
        dto.setCurrency(Currency.USD);

        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setId(2L);
        category.setType(BudgetType.INCOME);

        Budget existingBudget = new Budget();
        existingBudget.setPlannedAmount(200.0);
        existingBudget.setCurrency(Currency.EUR);
        existingBudget.setCategory(new Category());
        existingBudget.getCategory().setId(99L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(budgetRepository.existsByUserIdAndCategoryIdAndPeriodStart(1L, 2L, LocalDate.of(2024, 6, 1))).thenReturn(false);
        when(categoryService.getCategoryId("Uncategorized", 1L, BudgetType.INCOME)).thenReturn(29L);
        when(budgetRepository.findByUserIdAndCategoryIdAndPeriodStart(1L, 29L, LocalDate.of(2024, 6, 1)))
                .thenReturn(Optional.of(createTotalIncomeBudget()));

        when(budgetRepository.findAllByUserIdAndPeriodStartAndIncomeCategoriesExcludingCategory(1L, LocalDate.of(2024, 6, 1), 29L))
                .thenReturn(List.of(existingBudget));

        when(exchangeService.convert(200.0, Currency.EUR, Currency.UAH)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> budgetService.create(dto));
    }

    @Test
    void whenCreateIncomeBudget_sumExceedsTotal_thenThrow() {
        BudgetCreateDTO dto = new BudgetCreateDTO();
        dto.setUserId(1L);
        dto.setCategoryId(2L);
        dto.setPeriodStart(LocalDate.of(2024, 6, 1));
        dto.setPlannedAmount(100.0);
        dto.setCurrency(Currency.USD);

        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setId(2L);
        category.setType(BudgetType.INCOME);

        Budget existingBudget = new Budget();
        existingBudget.setPlannedAmount(4900.0);
        existingBudget.setCurrency(Currency.UAH);
        existingBudget.setCategory(new Category());
        existingBudget.getCategory().setId(99L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(budgetRepository.existsByUserIdAndCategoryIdAndPeriodStart(1L, 2L, LocalDate.of(2024, 6, 1))).thenReturn(false);
        when(categoryService.getCategoryId("Uncategorized", 1L, BudgetType.INCOME)).thenReturn(29L);
        when(budgetRepository.findByUserIdAndCategoryIdAndPeriodStart(1L, 29L, LocalDate.of(2024, 6, 1)))
                .thenReturn(Optional.of(createTotalIncomeBudget()));

        when(budgetRepository.findAllByUserIdAndPeriodStartAndIncomeCategoriesExcludingCategory(1L, LocalDate.of(2024, 6, 1), 29L))
                .thenReturn(List.of(existingBudget));

        when(exchangeService.convert(4900.0, Currency.UAH, Currency.UAH)).thenReturn(4900.0);
        when(exchangeService.convert(100.0, Currency.USD, Currency.UAH)).thenReturn(200.0);

        assertThrows(AlreadyExistsException.class, () -> budgetService.create(dto));
    }



    @Test
    void whenCopyBudgets_budgetExists_thenThrow() {
        BudgetCopyCreateDTO dto = new BudgetCopyCreateDTO();
        dto.setUserId(1L);
        dto.setFromPeriodStart(LocalDate.of(2024, 5, 1));
        dto.setToPeriodStart(LocalDate.of(2024, 6, 1));

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(budgetRepository.existsByUserIdAndPeriodStart(1L, LocalDate.of(2024, 6, 1))).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> budgetService.copyBudgets(dto));
    }

    @Test
    void whenCopyBudgets_userNotFound_thenThrow() {
        BudgetCopyCreateDTO dto = new BudgetCopyCreateDTO();
        dto.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> budgetService.copyBudgets(dto));
    }

    // === helpers ===

    private Budget createTotalExpenseBudget() {
        Budget b = new Budget();
        b.setCurrency(Currency.UAH);
        b.setPlannedAmount(5000.0);
        return b;
    }
}
