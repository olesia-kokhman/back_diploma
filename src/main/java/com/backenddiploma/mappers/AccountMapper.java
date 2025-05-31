package com.backenddiploma.mappers;

import com.backenddiploma.dto.account.AccountCreateDTO;
import com.backenddiploma.dto.account.AccountResponseDTO;
import com.backenddiploma.dto.account.AccountUpdateDTO;
import com.backenddiploma.models.accounts.*;
import com.backenddiploma.models.enums.AccountType;
import com.backenddiploma.services.AccountFactoryService;
import com.backenddiploma.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.backenddiploma.models.enums.Currency;

@Component
@RequiredArgsConstructor
public class AccountMapper {

    private final AccountFactoryService accountFactory;

    public Account toEntity(AccountCreateDTO dto, User user) {
        return accountFactory.createAccount(dto, user);
    }

    public void updateAccountFromDto(Account account, AccountUpdateDTO request) {
        if (request.getName() != null) account.setName(request.getName());
        if (request.getCurrency() != null) account.setCurrency(Currency.valueOf(request.getCurrency()));
        if (request.getBalance() != null) account.setBalance(request.getBalance());
        if (request.getIsMain() != null) account.setMain(request.getIsMain());


        if (account instanceof InvestmentAccount investment) {
            if (request.getQuantity() != null) investment.setQuantity(request.getQuantity());
            if (request.getBuyPrice() != null) investment.setBuyPrice(request.getBuyPrice());
            if (request.getBuyDate() != null) investment.setBuyDate(request.getBuyDate());
            if (request.getCurrentPrice() != null) investment.setCurrentPrice(request.getCurrentPrice());
            if (request.getPlatform() != null) investment.setPlatform(request.getPlatform());
        }

        if (account instanceof JarAccount jar) {
            if (request.getGoal() != null) jar.setGoal(request.getGoal());
        }

        if (account instanceof DebtAccount debt) {
            if (request.getLenderName() != null) debt.setLenderName(request.getLenderName());
            if (request.getInitialAmount() != null) debt.setInitialAmount(request.getInitialAmount());
            if (request.getCurrentAmount() != null) debt.setCurrentAmount(request.getCurrentAmount());
            if (request.getInterestRate() != null) debt.setInterestRate(request.getInterestRate());
            if (request.getStartDate() != null) debt.setStartDate(request.getStartDate());
            if (request.getDueDate() != null) debt.setDueDate(request.getDueDate());
            if (request.getIsRecurring() != null) debt.setRecurring(request.getIsRecurring());
        }
    }


    public AccountResponseDTO toResponse(Account account) {
        AccountResponseDTO response = new AccountResponseDTO();

        response.setId(account.getId());
        response.setName(account.getName());
        response.setCurrency(account.getCurrency().toString());
        response.setBalance(account.getBalance());
        response.setMain(account.isMain());
        response.setUserId(account.getUser() != null ? account.getUser().getId() : null);
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        response.setExternalAccountId(account.getExternalAccountId());

        if (account instanceof InvestmentAccount investment) {
            response.setAccountType(AccountType.INVESTMENT);
            response.setQuantity(investment.getQuantity());
            response.setBuyPrice(investment.getBuyPrice());
            response.setBuyDate(investment.getBuyDate());
            response.setCurrentPrice(investment.getCurrentPrice());
            response.setPlatform(investment.getPlatform());
        } else if (account instanceof JarAccount jar) {
            response.setAccountType(AccountType.JAR);
            response.setGoal(jar.getGoal());
        } else if (account instanceof DebtAccount debt) {
            response.setAccountType(AccountType.DEBT);
            response.setLenderName(debt.getLenderName());
            response.setInitialAmount(debt.getInitialAmount());
            response.setCurrentAmount(debt.getCurrentAmount());
            response.setInterestRate(debt.getInterestRate());
            response.setStartDate(debt.getStartDate());
            response.setDueDate(debt.getDueDate());
            response.setIsRecurring(debt.isRecurring());
        } else if (account instanceof BankAccount) {
            response.setAccountType(AccountType.BANK_ACCOUNT);
        } else if (account instanceof CashAccount) {
            response.setAccountType(AccountType.CASH);
        } else if (account instanceof RealEstateAccount) {
            response.setAccountType(AccountType.REAL_ESTATE);
        } else if (account instanceof TransportAccount) {
            response.setAccountType(AccountType.TRANSPORT);
        }

        return response;
    }

}
