package com.backenddiploma.unit.services;
import com.backenddiploma.dto.account.AccountCreateDTO;
import com.backenddiploma.models.User;
import com.backenddiploma.models.accounts.*;
import com.backenddiploma.models.enums.AccountType;
import com.backenddiploma.models.enums.Currency;
import com.backenddiploma.services.AccountFactoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AccountFactoryServiceTest {

    private AccountFactoryService factoryService;

    private User testUser;

    @BeforeEach
    void setup() {
        factoryService = new AccountFactoryService();

        testUser = new User();
        testUser.setId(1L);
    }

    @Test
    void whenCreateInvestmentAccount_thenFieldsAreSet() {
        AccountCreateDTO dto = new AccountCreateDTO();
        dto.setAccountType(AccountType.INVESTMENT);
        dto.setName("My Investment");
        dto.setCurrency(Currency.USD);
        dto.setBalance(5000.0);
        dto.setExternalAccountId("INV-123");

        dto.setQuantity(10.0);
        dto.setBuyPrice(100.0);
        dto.setBuyDate(LocalDate.of(2024, 1, 1));
        dto.setPlatform("eToro");
        dto.setCurrentPrice(120.0);

        Account account = factoryService.createAccount(dto, testUser);

        assertTrue(account instanceof InvestmentAccount);
        assertEquals("My Investment", account.getName());
        assertEquals(Currency.USD, account.getCurrency());
        assertEquals(5000.0, account.getBalance());
        assertEquals(testUser, account.getUser());
        assertEquals("INV-123", account.getExternalAccountId());

        InvestmentAccount inv = (InvestmentAccount) account;
        assertEquals(10.0, inv.getQuantity());
        assertEquals(100.0, inv.getBuyPrice());
        assertEquals(LocalDate.of(2024, 1, 1), inv.getBuyDate());
        assertEquals("eToro", inv.getPlatform());
        assertEquals(120.0, inv.getCurrentPrice());
    }

    @Test
    void whenCreateJarAccount_thenFieldsAreSet() {
        AccountCreateDTO dto = new AccountCreateDTO();
        dto.setAccountType(AccountType.JAR);
        dto.setName("Savings Jar");
        dto.setCurrency(Currency.EUR);
        dto.setBalance(1000.0);
        dto.setExternalAccountId("JAR-1");
        dto.setGoal(5000.0);

        Account account = factoryService.createAccount(dto, testUser);

        assertTrue(account instanceof JarAccount);
        JarAccount jar = (JarAccount) account;
        assertEquals(5000.0, jar.getGoal());
    }

    @Test
    void whenCreateDebtAccount_thenFieldsAreSet() {
        AccountCreateDTO dto = new AccountCreateDTO();
        dto.setAccountType(AccountType.DEBT);
        dto.setName("Car Loan");
        dto.setCurrency(Currency.UAH);
        dto.setBalance(20000.0);
        dto.setExternalAccountId("DEBT-999");

        dto.setLenderName("Bank XYZ");
        dto.setInitialAmount(30000.0);
        dto.setCurrentAmount(20000.0);
        dto.setInterestRate(5.5);
        dto.setStartDate(LocalDate.of(2023, 5, 1));
        dto.setDueDate(LocalDate.of(2026, 5, 1));
        dto.setRecurring(true);

        Account account = factoryService.createAccount(dto, testUser);

        assertTrue(account instanceof DebtAccount);
        DebtAccount debt = (DebtAccount) account;

        assertEquals("Bank XYZ", debt.getLenderName());
        assertEquals(30000.0, debt.getInitialAmount());
        assertEquals(20000.0, debt.getCurrentAmount());
        assertEquals(5.5, debt.getInterestRate());
        assertEquals(LocalDate.of(2023, 5, 1), debt.getStartDate());
        assertEquals(LocalDate.of(2026, 5, 1), debt.getDueDate());
        assertTrue(debt.isRecurring());
    }

    @Test
    void whenCreateBankAccount_thenCorrectType() {
        AccountCreateDTO dto = new AccountCreateDTO();
        dto.setAccountType(AccountType.BANK_ACCOUNT);
        dto.setName("Main Bank Account");
        dto.setCurrency(Currency.USD);
        dto.setBalance(15000.0);

        Account account = factoryService.createAccount(dto, testUser);

        assertTrue(account instanceof BankAccount);
    }

    @Test
    void whenCreateCashAccount_thenCorrectType() {
        AccountCreateDTO dto = new AccountCreateDTO();
        dto.setAccountType(AccountType.CASH);
        dto.setName("Wallet");
        dto.setCurrency(Currency.UAH);
        dto.setBalance(500.0);

        Account account = factoryService.createAccount(dto, testUser);

        assertTrue(account instanceof CashAccount);
    }

    @Test
    void whenCreateRealEstateAccount_thenCorrectType() {
        AccountCreateDTO dto = new AccountCreateDTO();
        dto.setAccountType(AccountType.REAL_ESTATE);
        dto.setName("Apartment");
        dto.setCurrency(Currency.EUR);
        dto.setBalance(100000.0);

        Account account = factoryService.createAccount(dto, testUser);

        assertTrue(account instanceof RealEstateAccount);
    }

    @Test
    void whenCreateTransportAccount_thenCorrectType() {
        AccountCreateDTO dto = new AccountCreateDTO();
        dto.setAccountType(AccountType.TRANSPORT);
        dto.setName("Car");
        dto.setCurrency(Currency.UAH);
        dto.setBalance(300000.0);

        Account account = factoryService.createAccount(dto, testUser);

        assertTrue(account instanceof TransportAccount);
    }

    @Test
    void whenUnsupportedAccountType_thenThrow() {
        AccountCreateDTO dto = new AccountCreateDTO();
        dto.setAccountType(null);

        assertThrows(NullPointerException.class, () ->
                factoryService.createAccount(dto, testUser)
        );
    }

}
