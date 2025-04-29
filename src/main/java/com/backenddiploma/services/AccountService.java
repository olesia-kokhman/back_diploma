package com.backenddiploma.services;

import com.backenddiploma.models.Account;
import com.backenddiploma.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public Account addAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account updateAccount(Long id, Account account) {
        if(accountRepository.existsById(id)) {
            account.setId(id);
            return accountRepository.save(account);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void deleteAccount(Long id) {
        if(accountRepository.existsById(id)) {
            accountRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
