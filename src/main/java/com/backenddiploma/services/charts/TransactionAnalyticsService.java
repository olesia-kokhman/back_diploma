package com.backenddiploma.services.charts;

import com.backenddiploma.dto.charts.*;
import com.backenddiploma.models.Category;
import com.backenddiploma.models.Transaction;
import com.backenddiploma.models.enums.Currency;
import com.backenddiploma.models.enums.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.backenddiploma.repositories.TransactionRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionAnalyticsService {

    private final TransactionRepository transactionRepository;
    private final ExchangeService exchanger;

    public ChartDataResponseDTO getAllTransactionsPerCurrentMonth(Long userId, TransactionType type) {
        LocalDate now = LocalDate.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = now.withDayOfMonth(now.lengthOfMonth()).atTime(23, 59, 59);

        List<Transaction> transactions = transactionRepository
                .findAllByUserIdAndTransferredAtBetween(userId, startOfMonth, endOfMonth)
                .stream()
                .filter(transaction -> transaction.getTransactionType() == type)
                .toList();

        Map<Category, List<Transaction>> grouped = transactions.stream().collect(Collectors.groupingBy(Transaction::getCategory));
        double total = transactions.stream()
                .map(transaction -> exchanger.convert(transaction.getAmount(), transaction.getCurrency(), Currency.UAH))
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .sum();

        List<IChartDTO> result = new ArrayList<>();
        for (Map.Entry<Category, List<Transaction>> entry : grouped.entrySet()) {
            Category category = entry.getKey();
            List<Transaction> categoryTransactions = entry.getValue();

            double categorySum = 0;
            for (Transaction transaction : categoryTransactions) {
                Double amountInUah = exchanger.convert(transaction.getAmount(), transaction.getCurrency(), Currency.UAH);
                if (amountInUah != null) {
                    categorySum += amountInUah;
                }
            }

            AllExpensesCategoryDTO categoryDTO = new AllExpensesCategoryDTO();
            categoryDTO.setCategoryTitle(category.getName());
            //categoryDTO.setColor(category.getColor());
            //categoryDTO.setIcon(category.getIcon());
            categoryDTO.setAmount(categorySum);
            //categoryDTO.setPercentage(total > 0 ? (categorySum / total * 100.0) : 0.0);
            result.add(categoryDTO);
        }

        ChartDataResponseDTO chartDataResponseDTO = new ChartDataResponseDTO();
        chartDataResponseDTO.setChartTitle("All expenses per " + (now.getMonth().name().substring(0, 1) +
                now.getMonth().name().substring(1).toLowerCase()));

        chartDataResponseDTO.setChartData(result);
        chartDataResponseDTO.setTotal(total);
        return chartDataResponseDTO;
    }

    public ChartDataResponseDTO getIncomeExpensePerHalfYear(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sixMonthsAgo = now.minusMonths(5).withDayOfMonth(1).toLocalDate().atStartOfDay();

        List<Transaction> transactions = transactionRepository
                .findAllByUserIdAndTransferredAtBetween(userId, sixMonthsAgo, now);

        Map<String, List<Transaction>> groupedByMonth = transactions.stream()
                .collect(Collectors.groupingBy(tx -> tx.getTransferredAt().getMonth().toString()));

        List<IChartDTO> result = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            LocalDateTime target = now.minusMonths(i).withDayOfMonth(1);
            String monthName = target.getMonth().toString();

            List<Transaction> monthTransactions = groupedByMonth.getOrDefault(monthName, List.of());

            double incomeSum = 0;
            double expenseSum = 0;

            for (Transaction transaction : monthTransactions) {
                Double amountInUah = exchanger.convert(transaction.getAmount(), transaction.getCurrency(), Currency.UAH);
                if (amountInUah == null) continue;

                if (transaction.getTransactionType() == TransactionType.INCOME) {
                    incomeSum += amountInUah;
                } else if (transaction.getTransactionType() == TransactionType.EXPENSE) {
                    expenseSum += amountInUah;
                }
            }

            result.add(new IncomeExpenseDTO((target.getMonth().name().substring(0, 1)
                    + target.getMonth().name().substring(1).toLowerCase()), expenseSum, incomeSum));
        }

        Collections.reverse(result);

        ChartDataResponseDTO chart = new ChartDataResponseDTO();
        chart.setChartTitle("Income & Expenses per last 6 months");
        chart.setChartData(result);
        return chart;
    }


    public ChartDataResponseDTO getGeneralBalance(Long userId,
                                                  Long accountId,
                                                  LocalDate from,
                                                  LocalDate to,
                                                  PeriodType groupBy) {

        LocalDate now = LocalDate.now();

        if (to == null) {
            to = now;
        }
        if (from == null) {
            from = to.minusMonths(5).withDayOfMonth(1);
        }

        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(23, 59, 59);

        List<Transaction> transactions = transactionRepository
                .findAllByUserIdAndTransferredAtBetween(userId, start, end)
                .stream()
                .filter(tx -> accountId == null || tx.getAccount().getId().equals(accountId))
                .toList();

        Map<String, List<Transaction>> groupedBy = transactions.stream()
                .collect(Collectors.groupingBy(tx -> {
                    LocalDateTime date = tx.getTransferredAt();
                    return switch (groupBy) {
                        case DAILY -> date.toLocalDate().toString();
                        case WEEKLY -> {
                            int year = date.getYear();
                            int week = date.get(WeekFields.ISO.weekOfWeekBasedYear());
                            yield String.format("Week %02d - %d", week, year);
                        }
                        case MONTHLY -> {
                            String month = date.getMonth().name().substring(0, 1)
                                    + date.getMonth().name().substring(1).toLowerCase();
                            yield String.format("%s %d", month, date.getYear());
                        }
                        case YEARLY -> String.valueOf(date.getYear());
                    };
                }));

        List<String> timeBuckets = new ArrayList<>();
        LocalDate current = from;
        while (!current.isAfter(to)) {
            String key = switch (groupBy) {
                case DAILY -> current.toString();
                case WEEKLY -> {
                    int year = current.getYear();
                    int week = current.get(WeekFields.ISO.weekOfWeekBasedYear());
                    yield String.format("Week %02d - %d", week, year);
                }
                case MONTHLY -> {
                    String month = current.getMonth().name().substring(0, 1)
                            + current.getMonth().name().substring(1).toLowerCase();
                    yield String.format("%s %d", month, current.getYear());
                }
                case YEARLY -> String.valueOf(current.getYear());
            };

            timeBuckets.add(key);

            current = switch (groupBy) {
                case DAILY -> current.plusDays(1);
                case WEEKLY -> current.plusWeeks(1);
                case MONTHLY -> current.plusMonths(1);
                case YEARLY -> current.plusYears(1);
            };
        }


        List<IChartDTO> result = new ArrayList<>();
        for (String key : timeBuckets) {
            List<Transaction> txs = groupedBy.getOrDefault(key, List.of());

            double income = 0;
            double expense = 0;

            for (Transaction tx : txs) {
                Double amount = exchanger.convert(tx.getAmount(), tx.getCurrency(), Currency.UAH);
                if (amount == null) continue;

                if (tx.getTransactionType() == TransactionType.INCOME) {
                    income += amount;
                } else if (tx.getTransactionType() == TransactionType.EXPENSE) {
                    expense += amount;
                }
            }

            double balance = income - expense;
            result.add(new GeneralBalanceDTO(key, balance));
        }

        ChartDataResponseDTO chart = new ChartDataResponseDTO();
        chart.setChartTitle(accountId != null
                ? "General balance for selected account"
                : "General balance");
        chart.setChartData(result);
        return chart;
    }





}
