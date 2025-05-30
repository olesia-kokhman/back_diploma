package com.backenddiploma.mappers;

import com.backenddiploma.dto.account.AccountCreateDTO;
import com.backenddiploma.dto.integrations.monobank.MonobankAccountDTO;
import com.backenddiploma.dto.integrations.monobank.MonobankJarDTO;
import com.backenddiploma.dto.integrations.monobank.MonobankTransactionDTO;
import com.backenddiploma.dto.integrations.monobank.MonobankUserInfoDTO;
import com.backenddiploma.dto.transaction.TransactionCreateDTO;
import com.backenddiploma.dto.user.UserUpdateDTO;
import com.backenddiploma.models.enums.AccountType;
import com.backenddiploma.models.enums.Currency;
import com.backenddiploma.models.enums.TransactionType;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class MonobankMapper {
    public AccountCreateDTO convertAccountToAccountCreateDTO(MonobankAccountDTO userInfoDTO, Long userId) {
        Currency currency = mapCurrency(userInfoDTO.getCurrencyCode());
        AccountCreateDTO accountCreateDTO = new AccountCreateDTO();
        accountCreateDTO.setName("Monobank " + currency + ", " + userInfoDTO.getType().toString());
        accountCreateDTO.setAccountType(AccountType.BANK_ACCOUNT);
        accountCreateDTO.setCurrency(currency);
        accountCreateDTO.setBalance((float)userInfoDTO.getBalance() / 100);
        accountCreateDTO.setUserId(userId);
        accountCreateDTO.setMain(false);
        return accountCreateDTO;
    }

    public AccountCreateDTO convertJarToAccountCreateDTO(MonobankJarDTO jarDTO, Long userId) {
        AccountCreateDTO accountCreateDTO = new AccountCreateDTO();
        Currency currency = mapCurrency(jarDTO.getCurrencyCode());
        accountCreateDTO.setName("Monobank " + currency + ", " + jarDTO.getTitle());
        accountCreateDTO.setAccountType(AccountType.JAR);
        accountCreateDTO.setMain(false);
        accountCreateDTO.setBalance((float) jarDTO.getBalance() / 100);
        accountCreateDTO.setUserId(userId);
        accountCreateDTO.setCurrency(currency);
        return accountCreateDTO;
    }

    public UserUpdateDTO convertToUserUpdateDTO(MonobankUserInfoDTO userInfoDTO) {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setWebHookUrl(userInfoDTO.getWebHookUrl());
        return userUpdateDTO;
    }

    public TransactionCreateDTO convertToTransactionCreateDTO(MonobankTransactionDTO transactionDTO, Long userId, Long accountId, Long categoryId) {
        TransactionCreateDTO transactionCreateDTO = new TransactionCreateDTO();
        if(transactionDTO.getOperationAmount() < 0) {
            transactionCreateDTO.setTransactionType(TransactionType.EXPENSE);
        } else {
            transactionCreateDTO.setTransactionType(TransactionType.INCOME);
        }
        transactionCreateDTO.setAmount((double) (transactionDTO.getOperationAmount() / 100));
        transactionCreateDTO.setUserId(userId);
        transactionCreateDTO.setDescription(transactionDTO.getDescription());
        transactionCreateDTO.setCurrency(mapCurrency(transactionDTO.getCurrencyCode()));
        transactionCreateDTO.setTransferredAt(LocalDateTime.ofInstant(Instant.ofEpochSecond(transactionDTO.getTime()),
                                              ZoneId.systemDefault()));

        transactionCreateDTO.setCategoryId(categoryId);
        transactionCreateDTO.setAccountId(accountId);

        return transactionCreateDTO;
    }

    private Currency mapCurrency(int currencyCode) {
        switch (currencyCode) {
            case 980:
                return Currency.UAH;
            case 840:
                return Currency.USD;
            case 978:
                return Currency.EUR;
            default:
                return Currency.UAH;
        }

    }

}
