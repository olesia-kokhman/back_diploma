package com.backenddiploma.unit.services;

import com.backenddiploma.dto.account.AccountCreateDTO;
import com.backenddiploma.dto.account.AccountResponseDTO;
import com.backenddiploma.dto.account.AccountUpdateDTO;
import com.backenddiploma.config.exceptions.NotFoundException;
import com.backenddiploma.mappers.AccountMapper;
import com.backenddiploma.models.accounts.Account;
import com.backenddiploma.models.User;
import com.backenddiploma.repositories.AccountRepository;
import com.backenddiploma.repositories.UserRepository;
import com.backenddiploma.services.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private UserRepository userRepository;

    // === create ===

    @Test
    void whenCreateAccount_thenSaveAndReturn() {
        AccountCreateDTO dto = new AccountCreateDTO();
        dto.setUserId(1L);

        User user = new User();
        user.setId(1L);

        Account account = new Account();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(accountMapper.toEntity(dto, user)).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);
        when(accountMapper.toResponse(account)).thenReturn(new AccountResponseDTO());

        AccountResponseDTO result = accountService.create(dto);

        assertNotNull(result);
        verify(accountRepository).save(account);
    }

    @Test
    void whenCreateAccount_userNotFound_thenThrow() {
        AccountCreateDTO dto = new AccountCreateDTO();
        dto.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> accountService.create(dto));
    }

    // === getById ===

    @Test
    void whenGetById_thenReturnAccount() {
        Account account = new Account();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountMapper.toResponse(account)).thenReturn(new AccountResponseDTO());

        AccountResponseDTO result = accountService.getById(1L);

        assertNotNull(result);
    }

    @Test
    void whenGetById_notFound_thenThrow() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> accountService.getById(1L));
    }

    // === update ===

    @Test
    void whenUpdateAccount_thenSaveAndReturn() {
        Account account = new Account();
        AccountUpdateDTO dto = new AccountUpdateDTO();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);
        when(accountMapper.toResponse(account)).thenReturn(new AccountResponseDTO());

        AccountResponseDTO result = accountService.update(1L, dto);

        assertNotNull(result);
        verify(accountMapper).updateAccountFromDto(account, dto);
        verify(accountRepository).save(account);
    }

    @Test
    void whenUpdateAccount_notFound_thenThrow() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> accountService.update(1L, new AccountUpdateDTO()));
    }

    // === delete ===

    @Test
    void whenDeleteAccount_thenDelete() {
        Account account = new Account();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        accountService.delete(1L);

        verify(accountRepository).delete(account);
    }

    @Test
    void whenDeleteAccount_notFound_thenThrow() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> accountService.delete(1L));
    }

    // === getAllByUser ===

    @Test
    void whenGetAllByUser_thenReturnList() {
        Account account1 = new Account();
        Account account2 = new Account();

        when(accountRepository.findAllByUserId(1L)).thenReturn(List.of(account1, account2));
        when(accountMapper.toResponse(account1)).thenReturn(new AccountResponseDTO());
        when(accountMapper.toResponse(account2)).thenReturn(new AccountResponseDTO());

        List<AccountResponseDTO> result = accountService.getAllByUser(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
