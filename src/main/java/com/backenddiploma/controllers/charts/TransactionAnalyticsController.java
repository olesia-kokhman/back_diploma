package com.backenddiploma.controllers.charts;

import com.backenddiploma.dto.charts.ChartDataResponseDTO;
import com.backenddiploma.models.enums.TransactionType;
import com.backenddiploma.services.charts.TransactionAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class TransactionAnalyticsController {

    private final TransactionAnalyticsService transactionAnalyticsService;

    @GetMapping("/all-expenses")
    public ChartDataResponseDTO getAllExpensesPerCurrentMonth(@RequestParam Long userId) {
        return transactionAnalyticsService.getAllTransactionsPerCurrentMonth(userId, TransactionType.EXPENSE);
    }

    @GetMapping("/all-incomes")
    public ChartDataResponseDTO getAllIncomesPerCurrentMonth(@RequestParam Long userId) {
        return transactionAnalyticsService.getAllTransactionsPerCurrentMonth(userId, TransactionType.INCOME);
    }

    @GetMapping("/income-expense")
    public ChartDataResponseDTO getIncomeExpensePerHalfYear(@RequestParam Long userId) {
        return transactionAnalyticsService.getIncomeExpensePerHalfYear(userId);
    }

    @GetMapping("/general-balance")
    public ChartDataResponseDTO getGeneralBalancePerHalfYear(@RequestParam Long userId,
                                                             @RequestParam(required = false) Long accountId,
                                                             @RequestParam(required = false) LocalDate start,
                                                             @RequestParam(required = false) LocalDate end) {
        return transactionAnalyticsService.getGeneralBalance(userId, accountId, start, end);
    }




}
