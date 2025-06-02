package com.backenddiploma.unit.services;
import com.backenddiploma.dto.charts.*;
import com.backenddiploma.models.Category;
import com.backenddiploma.models.Transaction;
import com.backenddiploma.models.User;
import com.backenddiploma.models.accounts.Account;
import com.backenddiploma.models.enums.Currency;
import com.backenddiploma.models.enums.TransactionType;
import com.backenddiploma.repositories.TransactionRepository;
import com.backenddiploma.services.charts.ExchangeService;
import com.backenddiploma.services.charts.TransactionAnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionAnalyticsServiceTest {

    @InjectMocks
    private TransactionAnalyticsService analyticsService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ExchangeService exchangeService;

    private Transaction transactionExample;

    @BeforeEach
    void setup() {
        transactionExample = new Transaction();
        transactionExample.setId(1L);
        transactionExample.setAmount(100.0);
        transactionExample.setCurrency(Currency.USD);
        transactionExample.setTransactionType(TransactionType.EXPENSE);
        transactionExample.setTransferredAt(LocalDateTime.now());
        Category category = new Category();
        category.setName("Food");
        category.setColor("#FF0000");
        category.setIconUrl("http://example.com/icon.png");
        transactionExample.setCategory(category);
        Account account = new Account();
        account.setId(1L);
        transactionExample.setAccount(account);
    }

    // === getAllTransactionsPerCurrentMonth ===

    @Test
    void whenGetAllTransactionsPerCurrentMonth_thenReturnData() {
        when(transactionRepository.findAllByUserIdAndTransferredAtBetween(
                1L,
                LocalDate.now().withDayOfMonth(1).atStartOfDay(),
                LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atTime(23, 59, 59)
        )).thenReturn(List.of(transactionExample));

        when(exchangeService.convert(100.0, Currency.USD, Currency.UAH)).thenReturn(4000.0);

        ChartDataResponseDTO result = analyticsService.getAllTransactionsPerCurrentMonth(1L, TransactionType.EXPENSE);

        assertNotNull(result);
        assertEquals("All expenses per " + LocalDate.now().getMonth().name().substring(0, 1)
                + LocalDate.now().getMonth().name().substring(1).toLowerCase(), result.getChartTitle());
        assertEquals(4000.0, result.getTotal());
        assertEquals(1, result.getChartData().size());
    }

    @Test
    void whenGetAllTransactionsPerCurrentMonth_noTransactions_thenEmptyChart() {
        when(transactionRepository.findAllByUserIdAndTransferredAtBetween(anyLong(), any(), any()))
                .thenReturn(List.of());

        ChartDataResponseDTO result = analyticsService.getAllTransactionsPerCurrentMonth(1L, TransactionType.EXPENSE);

        assertNotNull(result);
        assertEquals(0.0, result.getTotal());
        assertTrue(result.getChartData().isEmpty());
    }

    // === getIncomeExpense ===

    @Test
    void whenGetIncomeExpense_monthly_thenReturnData() {
        Transaction incomeTx = new Transaction();
        incomeTx.setAmount(200.0);
        incomeTx.setCurrency(Currency.USD);
        incomeTx.setTransactionType(TransactionType.INCOME);
        incomeTx.setTransferredAt(LocalDateTime.now());
        incomeTx.setAccount(transactionExample.getAccount());
        incomeTx.setCategory(transactionExample.getCategory());

        when(transactionRepository.findAllByUserIdAndTransferredAtBetween(anyLong(), any(), any()))
                .thenReturn(List.of(transactionExample, incomeTx));

        when(exchangeService.convert(100.0, Currency.USD, Currency.UAH)).thenReturn(4000.0);
        when(exchangeService.convert(200.0, Currency.USD, Currency.UAH)).thenReturn(8000.0);

        ChartDataResponseDTO result = analyticsService.getIncomeExpense(
                1L,
                null,
                null,
                null,
                PeriodType.MONTHLY,
                false,
                false
        );

        assertNotNull(result);
        assertEquals(6, result.getChartData().size());
    }

    @Test
    void whenGetIncomeExpense_noTransactions_thenEmptyChart() {
        when(transactionRepository.findAllByUserIdAndTransferredAtBetween(anyLong(), any(), any()))
                .thenReturn(List.of());

        ChartDataResponseDTO result = analyticsService.getIncomeExpense(
                1L,
                null,
                null,
                null,
                PeriodType.MONTHLY,
                false,
                false
        );

        assertNotNull(result);
        assertEquals(6, result.getChartData().size());
    }

    // === getGeneralBalance ===

    @Test
    void whenGetGeneralBalance_thenReturnData() {
        Transaction incomeTx = new Transaction();
        incomeTx.setAmount(200.0);
        incomeTx.setCurrency(Currency.USD);
        incomeTx.setTransactionType(TransactionType.INCOME);
        incomeTx.setTransferredAt(LocalDateTime.now());
        incomeTx.setAccount(transactionExample.getAccount());

        when(transactionRepository.findAllByUserIdAndTransferredAtBetween(anyLong(), any(), any()))
                .thenReturn(List.of(transactionExample, incomeTx));

        when(exchangeService.convert(100.0, Currency.USD, Currency.UAH)).thenReturn(4000.0);
        when(exchangeService.convert(200.0, Currency.USD, Currency.UAH)).thenReturn(8000.0);

        ChartDataResponseDTO result = analyticsService.getGeneralBalance(
                1L,
                null,
                null,
                null,
                PeriodType.MONTHLY
        );

        assertNotNull(result);
        assertEquals(6, result.getChartData().size());
    }

    @Test
    void whenGetGeneralBalance_noTransactions_thenEmptyChart() {
        when(transactionRepository.findAllByUserIdAndTransferredAtBetween(anyLong(), any(), any()))
                .thenReturn(List.of());

        ChartDataResponseDTO result = analyticsService.getGeneralBalance(
                1L,
                null,
                null,
                null,
                PeriodType.MONTHLY
        );

        assertNotNull(result);
        assertEquals(6, result.getChartData().size());
    }
}

