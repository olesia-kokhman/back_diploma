package com.backenddiploma.services;

import com.backenddiploma.dto.account.AccountCreateDTO;
import com.backenddiploma.models.accounts.*;
import com.backenddiploma.models.User;
import com.backenddiploma.models.enums.Currency;
import org.springframework.stereotype.Service;

@Service
public class AccountFactoryService {

    public Account createAccount(AccountCreateDTO request, User user) {
        Account account;

        switch (request.getAccountType()) {
            case INVESTMENT -> {
                InvestmentAccount investment = new InvestmentAccount();
                investment.setQuantity(request.getQuantity());
                investment.setBuyPrice(request.getBuyPrice());
                investment.setBuyDate(request.getBuyDate());
                investment.setPlatform(request.getPlatform());
                investment.setCurrentPrice(request.getCurrentPrice());
                account = investment;
            }
            case JAR -> {
                JarAccount jar = new JarAccount();
                jar.setGoal(request.getGoal());
                account = jar;
            }
            case DEBT -> {
                DebtAccount debt = new DebtAccount();
                debt.setLenderName(request.getLenderName());
                debt.setInitialAmount(request.getInitialAmount());
                debt.setCurrentAmount(request.getCurrentAmount());
                debt.setInterestRate(request.getInterestRate());
                debt.setStartDate(request.getStartDate());
                debt.setDueDate(request.getDueDate());
                debt.setRecurring(request.isRecurring());
                account = debt;
            }
            case BANK_ACCOUNT -> {
                BankAccount bank = new BankAccount();
                account = bank;
            }

            case CASH -> {
                CashAccount cash = new CashAccount();
                account = cash;
            }

            case REAL_ESTATE -> {
                RealEstateAccount estate = new RealEstateAccount();
                account = estate;
            }

            case TRANSPORT -> {
                TransportAccount transport = new TransportAccount();
                account = transport;
            }

            default -> throw new IllegalArgumentException("Unsupported account type: " + request.getAccountType());
        }

        account.setName(request.getName());
        account.setCurrency(request.getCurrency());
        account.setBalance(request.getBalance());
        account.setMain(request.isMain());
        account.setUser(user);
        return account;
    }
}
