package com.backenddiploma.unit.mappers;

import com.backenddiploma.dto.account.AccountCreateDTO;
import com.backenddiploma.dto.integrations.monobank.MonobankAccountDTO;
import com.backenddiploma.dto.integrations.monobank.MonobankJarDTO;
import com.backenddiploma.dto.integrations.monobank.MonobankTransactionDTO;
import com.backenddiploma.dto.integrations.monobank.MonobankUserInfoDTO;
import com.backenddiploma.dto.transaction.TransactionCreateDTO;
import com.backenddiploma.dto.user.UserUpdateDTO;
import com.backenddiploma.mappers.MonobankMapper;
import com.backenddiploma.models.enums.AccountType;
import com.backenddiploma.models.enums.BudgetType;
import com.backenddiploma.models.enums.Currency;
import com.backenddiploma.models.enums.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

class MonobankMapperTest {

    private MonobankMapper monobankMapper;

    @BeforeEach
    void setUp() {
        monobankMapper = new MonobankMapper();
    }


    @Test
    void testConvertJarToAccountCreateDTO() {
        MonobankJarDTO jarDTO = new MonobankJarDTO();
        jarDTO.setTitle("Vacation Fund");
        jarDTO.setCurrencyCode(978);
        jarDTO.setBalance(50000);

        Long userId = 99L;

        AccountCreateDTO result = monobankMapper.convertJarToAccountCreateDTO(jarDTO, userId);

        assertThat(result.getName()).isEqualTo("Monobank EUR, Vacation Fund");
        assertThat(result.getAccountType()).isEqualTo(AccountType.JAR);
        assertThat(result.getBalance()).isEqualTo(500.0f);
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getCurrency()).isEqualTo(Currency.EUR);
    }

    @Test
    void testConvertToUserUpdateDTO() {
        MonobankUserInfoDTO userInfoDTO = new MonobankUserInfoDTO();
        userInfoDTO.setWebHookUrl("https://webhook.url");

        UserUpdateDTO result = monobankMapper.convertToUserUpdateDTO(userInfoDTO);

        assertThat(result.getWebHookUrl()).isEqualTo("https://webhook.url");
    }

    @Test
    void testConvertToTransactionCreateDTO_expense() {
        MonobankTransactionDTO transactionDTO = new MonobankTransactionDTO();
        transactionDTO.setOperationAmount(-54321);
        transactionDTO.setCurrencyCode(840);
        transactionDTO.setDescription("Coffee Shop");
        transactionDTO.setMcc(5812);
        transactionDTO.setTime(Instant.now().getEpochSecond());

        Long userId = 100L;
        Long accountId = 200L;
        Long categoryId = 300L;

        TransactionCreateDTO result = monobankMapper.convertToTransactionCreateDTO(transactionDTO, userId, accountId, categoryId);

        assertThat(result.getTransactionType()).isEqualTo(TransactionType.EXPENSE);
        assertThat(result.getAmount()).isEqualTo(543.0);
        assertThat(result.getCurrency()).isEqualTo(Currency.USD);
        assertThat(result.getDescription()).isEqualTo("Coffee Shop");
        assertThat(result.getCategoryId()).isEqualTo(categoryId);
        assertThat(result.getAccountId()).isEqualTo(accountId);
        assertThat(result.getUserId()).isEqualTo(userId);

        // check date conversion roughly (same day)
        LocalDateTime expectedDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(transactionDTO.getTime()), ZoneId.systemDefault());
        assertThat(result.getTransferredAt().toLocalDate()).isEqualTo(expectedDate.toLocalDate());
    }

    @Test
    void testConvertToTransactionCreateDTO_income() {
        MonobankTransactionDTO transactionDTO = new MonobankTransactionDTO();
        transactionDTO.setOperationAmount(150000);
        transactionDTO.setCurrencyCode(980);
        transactionDTO.setDescription("Salary");
        transactionDTO.setMcc(0);
        transactionDTO.setTime(Instant.now().getEpochSecond());

        Long userId = 1L;
        Long accountId = 2L;
        Long categoryId = 3L;

        TransactionCreateDTO result = monobankMapper.convertToTransactionCreateDTO(transactionDTO, userId, accountId, categoryId);

        assertThat(result.getTransactionType()).isEqualTo(TransactionType.INCOME);
        assertThat(result.getAmount()).isEqualTo(1500.00);
        assertThat(result.getCurrency()).isEqualTo(Currency.UAH);
        assertThat(result.getDescription()).isEqualTo("Salary");
        assertThat(result.getCategoryId()).isEqualTo(categoryId);
        assertThat(result.getAccountId()).isEqualTo(accountId);
        assertThat(result.getUserId()).isEqualTo(userId);

        // check date conversion roughly (same day)
        LocalDateTime expectedDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(transactionDTO.getTime()), ZoneId.systemDefault());
        assertThat(result.getTransferredAt().toLocalDate()).isEqualTo(expectedDate.toLocalDate());
    }
}
