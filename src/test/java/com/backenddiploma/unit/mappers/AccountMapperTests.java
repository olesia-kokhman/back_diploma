package com.backenddiploma.unit.mappers;

import com.backenddiploma.dto.account.AccountCreateDTO;
import com.backenddiploma.dto.account.AccountResponseDTO;
import com.backenddiploma.dto.account.AccountUpdateDTO;
import com.backenddiploma.mappers.AccountMapper;
import com.backenddiploma.models.User;
import com.backenddiploma.models.accounts.*;
import com.backenddiploma.models.enums.AccountType;
import com.backenddiploma.models.enums.Currency;
import com.backenddiploma.services.AccountFactoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AccountMapperTest {

    private AccountFactoryService accountFactory;
    private AccountMapper accountMapper;

    @BeforeEach
    void setUp() {
        accountFactory = Mockito.mock(AccountFactoryService.class);
        accountMapper = new AccountMapper(accountFactory);
    }

    @Test
    void testToEntity() {
        AccountCreateDTO dto = new AccountCreateDTO();
        User user = new User();

        Account expectedAccount = new BankAccount();
        when(accountFactory.createAccount(dto, user)).thenReturn(expectedAccount);

        Account result = accountMapper.toEntity(dto, user);

        assertThat(result).isSameAs(expectedAccount);
        verify(accountFactory, times(1)).createAccount(dto, user);
    }

    @Test
    void testUpdateInvestmentAccountFromDto() {
        InvestmentAccount account = new InvestmentAccount();
        AccountUpdateDTO updateDTO = new AccountUpdateDTO();
        updateDTO.setName("My Investment");
        updateDTO.setCurrency("USD");
        updateDTO.setBalance(1000.0);
        updateDTO.setQuantity(10.0);
        updateDTO.setBuyPrice(50.0);
        updateDTO.setBuyDate(LocalDate.now());
        updateDTO.setCurrentPrice(60.0);
        updateDTO.setPlatform("Interactive Brokers");

        accountMapper.updateAccountFromDto(account, updateDTO);

        assertThat(account.getName()).isEqualTo("My Investment");
        assertThat(account.getCurrency()).isEqualTo(Currency.USD);
        assertThat(account.getBalance()).isEqualByComparingTo(1000.0);
        assertThat(account.getQuantity()).isEqualTo(10);
        assertThat(account.getBuyPrice()).isEqualByComparingTo(50.0);
        assertThat(account.getBuyDate()).isEqualTo(updateDTO.getBuyDate());
        assertThat(account.getCurrentPrice()).isEqualByComparingTo(60.0);
        assertThat(account.getPlatform()).isEqualTo("Interactive Brokers");
    }

    @Test
    void testUpdateJarAccountFromDto() {
        JarAccount account = new JarAccount();
        AccountUpdateDTO updateDTO = new AccountUpdateDTO();
        updateDTO.setName("My Jar");
        updateDTO.setCurrency("EUR");
        updateDTO.setBalance(500.0);
        updateDTO.setGoal(1000.0);

        accountMapper.updateAccountFromDto(account, updateDTO);

        assertThat(account.getName()).isEqualTo("My Jar");
        assertThat(account.getCurrency()).isEqualTo(Currency.EUR);
        assertThat(account.getBalance()).isEqualByComparingTo(500.0);
        assertThat(account.getGoal()).isEqualByComparingTo(1000.0);
    }

    @Test
    void testUpdateDebtAccountFromDto() {
        DebtAccount account = new DebtAccount();
        AccountUpdateDTO updateDTO = new AccountUpdateDTO();
        updateDTO.setName("My Debt");
        updateDTO.setCurrency("UAH");
        updateDTO.setBalance(3000.0);
        updateDTO.setLenderName("Bank XYZ");
        updateDTO.setInitialAmount(5000.0);
        updateDTO.setCurrentAmount(3000.0);
        updateDTO.setInterestRate(10.5);
        updateDTO.setStartDate(LocalDate.of(2024, 1, 1));
        updateDTO.setDueDate(LocalDate.of(2025, 1, 1));
        updateDTO.setIsRecurring(true);

        accountMapper.updateAccountFromDto(account, updateDTO);

        assertThat(account.getName()).isEqualTo("My Debt");
        assertThat(account.getCurrency()).isEqualTo(Currency.UAH);
        assertThat(account.getBalance()).isEqualByComparingTo(3000.0);
        assertThat(account.getLenderName()).isEqualTo("Bank XYZ");
        assertThat(account.getInitialAmount()).isEqualByComparingTo(5000.0);
        assertThat(account.getCurrentAmount()).isEqualByComparingTo(3000.0);
        assertThat(account.getInterestRate()).isEqualTo(10.5);
        assertThat(account.getStartDate()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(account.getDueDate()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(account.isRecurring()).isTrue();
    }

    @Test
    void testToResponseForBankAccount() {
        BankAccount account = new BankAccount();
        account.setId(1L);
        account.setName("Bank acc");
        account.setCurrency(Currency.USD);
        account.setBalance(1000.0);
        User user = new User();
        user.setId(10L);
        account.setUser(user);

        AccountResponseDTO response = accountMapper.toResponse(account);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Bank acc");
        assertThat(response.getCurrency()).isEqualTo("USD");
        assertThat(response.getBalance()).isEqualByComparingTo(1000.0);
        assertThat(response.getUserId()).isEqualTo(10L);
        assertThat(response.getAccountType()).isEqualTo(AccountType.BANK_ACCOUNT);
    }

    @Test
    void testToResponseForJarAccount() {
        JarAccount account = new JarAccount();
        account.setId(2L);
        account.setName("Jar");
        account.setCurrency(Currency.EUR);
        account.setBalance(200.0);
        User user = new User();
        user.setId(11L);
        account.setUser(user);
        account.setGoal(500.0);

        AccountResponseDTO response = accountMapper.toResponse(account);

        assertThat(response.getId()).isEqualTo(2L);
        assertThat(response.getName()).isEqualTo("Jar");
        assertThat(response.getCurrency()).isEqualTo("EUR");
        assertThat(response.getBalance()).isEqualByComparingTo(200.0);
        assertThat(response.getUserId()).isEqualTo(11L);
        assertThat(response.getAccountType()).isEqualTo(AccountType.JAR);
        assertThat(response.getGoal()).isEqualByComparingTo(500.0);
    }

    @Test
    void testToResponseForDebtAccount() {
        DebtAccount account = new DebtAccount();
        account.setId(3L);
        account.setName("Debt");
        account.setCurrency(Currency.UAH);
        account.setBalance(4000.0);
        User user = new User();
        user.setId(12L);
        account.setUser(user);
        account.setLenderName("Private lender");
        account.setInitialAmount(6000.0);
        account.setCurrentAmount(4000.0);
        account.setInterestRate(7.5);
        account.setStartDate(LocalDate.of(2023, 5, 1));
        account.setDueDate(LocalDate.of(2024, 5, 1));
        account.setRecurring(false);

        AccountResponseDTO response = accountMapper.toResponse(account);

        assertThat(response.getId()).isEqualTo(3L);
        assertThat(response.getName()).isEqualTo("Debt");
        assertThat(response.getCurrency()).isEqualTo("UAH");
        assertThat(response.getBalance()).isEqualByComparingTo(4000.0);
        assertThat(response.getUserId()).isEqualTo(12L);
        assertThat(response.getAccountType()).isEqualTo(AccountType.DEBT);
        assertThat(response.getLenderName()).isEqualTo("Private lender");
        assertThat(response.getInitialAmount()).isEqualByComparingTo(6000.0);
        assertThat(response.getCurrentAmount()).isEqualByComparingTo(4000.0);
        assertThat(response.getInterestRate()).isEqualTo(7.5);
        assertThat(response.getStartDate()).isEqualTo(LocalDate.of(2023, 5, 1));
        assertThat(response.getDueDate()).isEqualTo(LocalDate.of(2024, 5, 1));
        assertThat(response.getIsRecurring()).isFalse();
    }
}
