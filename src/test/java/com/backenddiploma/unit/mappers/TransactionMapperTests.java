package com.backenddiploma.unit.mappers;

import com.backenddiploma.dto.transaction.TransactionCreateDTO;
import com.backenddiploma.dto.transaction.TransactionResponseDTO;
import com.backenddiploma.dto.transaction.TransactionUpdateDTO;
import com.backenddiploma.mappers.TransactionMapper;
import com.backenddiploma.models.*;
import com.backenddiploma.models.accounts.Account;
import com.backenddiploma.models.enums.Currency;
import com.backenddiploma.models.enums.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionMapperTest {

    private TransactionMapper transactionMapper;

    @BeforeEach
    void setUp() {
        transactionMapper = new TransactionMapper();
    }

    @Test
    void testToEntity() {
        TransactionCreateDTO dto = new TransactionCreateDTO();
        dto.setTransactionType(TransactionType.EXPENSE);
        dto.setAmount(250.0);
        dto.setCurrency(Currency.USD);
        dto.setDescription("Dinner at restaurant");
        dto.setTransferredAt(LocalDateTime.of(2025, 6, 1, 18, 30));

        Account account = new Account();
        account.setId(1L);
        account.setName("Main Account");

        Category category = new Category();
        category.setId(2L);
        category.setName("Food");

        User user = new User();
        user.setId(3L);

        Transaction transaction = transactionMapper.toEntity(dto, account, category, user);

        assertThat(transaction.getTransactionType()).isEqualTo(TransactionType.EXPENSE);
        assertThat(transaction.getAmount()).isEqualTo(250.0);
        assertThat(transaction.getCurrency()).isEqualTo(Currency.USD);
        assertThat(transaction.getDescription()).isEqualTo("Dinner at restaurant");
        assertThat(transaction.getAccount()).isEqualTo(account);
        assertThat(transaction.getCategory()).isEqualTo(category);
        assertThat(transaction.getUser()).isEqualTo(user);
        assertThat(transaction.getTransferredAt()).isEqualTo(LocalDateTime.of(2025, 6, 1, 18, 30));
    }

    @Test
    void testUpdateTransactionFromDto() {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.INCOME);
        transaction.setAmount(1000.0);
        transaction.setCurrency(Currency.UAH);
        transaction.setDescription("Salary");
        transaction.setTransferredAt(LocalDateTime.of(2025, 5, 1, 9, 0));

        Account oldAccount = new Account();
        oldAccount.setId(1L);
        oldAccount.setName("Old Account");

        Category oldCategory = new Category();
        oldCategory.setId(2L);
        oldCategory.setName("Old Category");

        transaction.setAccount(oldAccount);
        transaction.setCategory(oldCategory);

        // Now update
        TransactionUpdateDTO dto = new TransactionUpdateDTO();
        dto.setTransactionType(TransactionType.EXPENSE);
        dto.setAmount(200.0);
        dto.setCurrency(Currency.EUR);
        dto.setDescription("New expense - groceries");
        dto.setTransferredAt(LocalDateTime.of(2025, 6, 2, 15, 30));

        Account newAccount = new Account();
        newAccount.setId(10L);
        newAccount.setName("New Account");

        Category newCategory = new Category();
        newCategory.setId(20L);
        newCategory.setName("New Category");

        transactionMapper.updateTransactionFromDto(transaction, dto, newAccount, newCategory);

        assertThat(transaction.getTransactionType()).isEqualTo(TransactionType.EXPENSE);
        assertThat(transaction.getAmount()).isEqualTo(200.0);
        assertThat(transaction.getCurrency()).isEqualTo(Currency.EUR);
        assertThat(transaction.getDescription()).isEqualTo("New expense - groceries");
        assertThat(transaction.getTransferredAt()).isEqualTo(LocalDateTime.of(2025, 6, 2, 15, 30));
        assertThat(transaction.getAccount()).isEqualTo(newAccount);
        assertThat(transaction.getCategory()).isEqualTo(newCategory);
    }

    @Test
    void testToResponse() {
        Transaction transaction = new Transaction();
        transaction.setId(100L);
        transaction.setTransactionType(TransactionType.INCOME);
        transaction.setAmount(5000.0);
        transaction.setCurrency(Currency.USD);
        transaction.setDescription("Freelance Project");

        Account account = new Account();
        account.setId(11L);
        account.setName("Freelance Account");
        transaction.setAccount(account);

        Category category = new Category();
        category.setId(22L);
        category.setName("Work");
        transaction.setCategory(category);

        User user = new User();
        user.setId(33L);
        transaction.setUser(user);

        transaction.setTransferredAt(LocalDateTime.of(2025, 6, 1, 14, 0));
        transaction.setCreatedAt(LocalDateTime.of(2025, 6, 1, 15, 0));
        transaction.setUpdatedAt(LocalDateTime.of(2025, 6, 2, 16, 30));

        TransactionResponseDTO response = transactionMapper.toResponse(transaction);

        assertThat(response.getId()).isEqualTo(100L);
        assertThat(response.getTransactionType()).isEqualTo(TransactionType.INCOME);
        assertThat(response.getAmount()).isEqualTo(5000.0);
        assertThat(response.getCurrency()).isEqualTo(Currency.USD);
        assertThat(response.getDescription()).isEqualTo("Freelance Project");
        assertThat(response.getAccountId()).isEqualTo(11L);
        assertThat(response.getAccountName()).isEqualTo("Freelance Account");
        assertThat(response.getCategoryId()).isEqualTo(22L);
        assertThat(response.getCategoryName()).isEqualTo("Work");
        assertThat(response.getUserId()).isEqualTo(33L);
        assertThat(response.getTransferredAt()).isEqualTo(LocalDateTime.of(2025, 6, 1, 14, 0));
        assertThat(response.getCreatedAt()).isEqualTo(LocalDateTime.of(2025, 6, 1, 15, 0));
        assertThat(response.getUpdatedAt()).isEqualTo(LocalDateTime.of(2025, 6, 2, 16, 30));
    }
}
