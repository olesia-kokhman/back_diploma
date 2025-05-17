package com.backenddiploma.mappers;

import com.backenddiploma.dto.account.AccountCreateDTO;
import com.backenddiploma.dto.account.AccountResponseDTO;
import com.backenddiploma.dto.account.AccountUpdateDTO;
import com.backenddiploma.models.Account;
import com.backenddiploma.models.User;
import org.springframework.stereotype.Component;

import com.backenddiploma.models.enums.Currency;


@Component
public class AccountMapper {

    public Account toEntity(AccountCreateDTO request, User user) {
        Account account = new Account();
        account.setName(request.getName());
        account.setAccountType(request.getAccountType());
        account.setCurrency(Currency.valueOf(request.getCurrency()));
        account.setBalance(request.getBalance());
        account.setMain(request.isMain());
        account.setUser(user);
        return account;
    }

    public void updateAccountFromDto(Account account, AccountUpdateDTO request) {
        if (request.getName() != null) {
            account.setName(request.getName());
        }
        if (request.getAccountType() != null) {
            account.setAccountType(request.getAccountType());
        }
        if (request.getCurrency() != null) {
            account.setCurrency(Currency.valueOf(request.getCurrency()));
        }
        if (request.getBalance() != null) {
            account.setBalance(request.getBalance());
        }
        if (request.getIsMain() != null) {
            account.setMain(request.getIsMain());
        }
    }

    public AccountResponseDTO toResponse(Account account) {
        AccountResponseDTO response = new AccountResponseDTO();
        response.setId(account.getId());
        response.setName(account.getName());
        response.setAccountType(account.getAccountType());
        response.setCurrency(account.getCurrency().toString());
        response.setBalance(account.getBalance());
        response.setMain(account.isMain());
        response.setUserId(account.getUser() != null ? account.getUser().getId() : null);
        response.setCreatedAt(account.getCreatedAt());
        response.setUpdatedAt(account.getUpdatedAt());
        return response;
    }
}
