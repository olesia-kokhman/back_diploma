package com.backenddiploma.services;

import com.backenddiploma.dto.account.AccountCreateDTO;
import com.backenddiploma.dto.account.AccountResponseDTO;
import com.backenddiploma.dto.account.AccountUpdateDTO;
import com.backenddiploma.config.exceptions.NotFoundException;
import com.backenddiploma.mappers.AccountMapper;
import com.backenddiploma.models.accounts.Account;
import com.backenddiploma.models.User;
import com.backenddiploma.repositories.AccountRepository;
import com.backenddiploma.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final UserRepository userRepository;

    @Transactional
    public AccountResponseDTO create(AccountCreateDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + dto.getUserId()));

        Account account = accountMapper.toEntity(dto, user);
        Account savedAccount = accountRepository.save(account);

        return accountMapper.toResponse(savedAccount);
    }

    @Transactional(readOnly = true)
    public AccountResponseDTO getById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found with id: " + id));
        return accountMapper.toResponse(account);
    }

    @Transactional
    public AccountResponseDTO update(Long id, AccountUpdateDTO dto) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found with id: " + id));

        accountMapper.updateAccountFromDto(account, dto);
        Account updatedAccount = accountRepository.save(account);

        return accountMapper.toResponse(updatedAccount);
    }

    @Transactional
    public void delete(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found with id: " + id));
        accountRepository.delete(account);
    }

    @Transactional(readOnly = true)
    public List<AccountResponseDTO> getAllByUser(Long userId) {
        List<Account> accounts = accountRepository.findAllByUserId(userId);
        return accounts.stream().map(accountMapper::toResponse).collect(Collectors.toList());
    }
}
