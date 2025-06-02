package com.backenddiploma.unit.services;

import com.backenddiploma.config.exceptions.NotFoundException;
import com.backenddiploma.dto.transaction.TransactionCreateDTO;
import com.backenddiploma.dto.transaction.TransactionResponseDTO;
import com.backenddiploma.dto.transaction.TransactionUpdateDTO;
import com.backenddiploma.mappers.TransactionMapper;
import com.backenddiploma.models.Category;
import com.backenddiploma.models.Transaction;
import com.backenddiploma.models.User;
import com.backenddiploma.models.accounts.Account;
import com.backenddiploma.repositories.AccountRepository;
import com.backenddiploma.repositories.CategoryRepository;
import com.backenddiploma.repositories.TransactionRepository;
import com.backenddiploma.repositories.UserRepository;
import com.backenddiploma.services.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    // get by user id ==

    @Test
    void whenGetById_withValidUserId_thenReturnTransaction() {
        Long transactionId = 1L;
        Long userId = 10L;

        User user = new User();
        user.setId(userId);

        Transaction transaction = new Transaction();
        transaction.setId(transactionId);
        transaction.setUser(user);

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(transactionMapper.toResponse(transaction)).thenReturn(new TransactionResponseDTO());

        TransactionResponseDTO result = transactionService.getById(transactionId, userId);

        assertNotNull(result);
        verify(transactionRepository).findById(transactionId);
    }

    @Test
    void whenGetById_transactionNotFound_thenThrow() {
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> transactionService.getById(1L, 10L));
    }

    @Test
    void whenGetById_wrongUser_thenThrow() {
        Transaction transaction = new Transaction();
        User user = new User();
        user.setId(99L);  // wrong user id
        transaction.setUser(user);

        when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transaction));

        assertThrows(NotFoundException.class, () -> transactionService.getById(1L, 10L));
    }


    @Test
    void whenCreate_withValidInput_thenSaveTransaction() {
        TransactionCreateDTO dto = new TransactionCreateDTO();
        dto.setAccountId(1L);
        dto.setUserId(10L);
        dto.setCategoryId(2L);

        Account account = new Account();
        account.setId(1L);

        User user = new User();
        user.setId(10L);

        Category category = new Category();
        category.setId(2L);

        Transaction transaction = new Transaction();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(userRepository.findById(10L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(transactionMapper.toEntity(dto, account, category, user)).thenReturn(transaction);
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        when(transactionMapper.toResponse(transaction)).thenReturn(new TransactionResponseDTO());

        TransactionResponseDTO result = transactionService.create(dto);

        assertNotNull(result);
        verify(transactionRepository).save(transaction);
    }

    @Test
    void whenCreate_accountNotFound_thenThrow() {
        TransactionCreateDTO dto = new TransactionCreateDTO();
        dto.setAccountId(1L);

        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> transactionService.create(dto));
    }

    @Test
    void whenCreate_userNotFound_thenThrow() {
        TransactionCreateDTO dto = new TransactionCreateDTO();
        dto.setAccountId(1L);
        dto.setUserId(10L);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(new Account()));
        when(userRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> transactionService.create(dto));
    }

    @Test
    void whenCreate_categoryNotFound_thenThrow() {
        TransactionCreateDTO dto = new TransactionCreateDTO();
        dto.setAccountId(1L);
        dto.setUserId(10L);
        dto.setCategoryId(2L);

        Account account = new Account();
        account.setId(1L); // ОБОВʼЯЗКОВО!

        User user = new User();
        user.setId(10L); // ОБОВʼЯЗКОВО!

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(userRepository.findById(10L)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> transactionService.create(dto));
    }

    @Test
    void whenUpdate_withValidInput_thenSaveTransaction() {
        Long transactionId = 1L;
        Long userId = 10L;

        Transaction transaction = new Transaction();
        User user = new User();
        user.setId(userId);
        transaction.setUser(user);

        TransactionUpdateDTO dto = new TransactionUpdateDTO();
        dto.setAccountId(1L);
        dto.setCategoryId(2L);

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(new Account()));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(new Category()));
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        when(transactionMapper.toResponse(transaction)).thenReturn(new TransactionResponseDTO());

        TransactionResponseDTO result = transactionService.update(transactionId, dto, userId);

        assertNotNull(result);
        verify(transactionRepository).save(transaction);
    }

    @Test
    void whenUpdate_transactionNotFound_thenThrow() {
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> transactionService.update(1L, new TransactionUpdateDTO(), 10L));
    }

    @Test
    void whenUpdate_wrongUser_thenThrow() {
        Transaction transaction = new Transaction();
        User user = new User();
        user.setId(99L);  // wrong user id
        transaction.setUser(user);

        when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transaction));

        assertThrows(NotFoundException.class, () -> transactionService.update(1L, new TransactionUpdateDTO(), 10L));
    }

    @Test
    void whenUpdate_accountNotFound_thenThrow() {
        Long transactionId = 1L;
        Long userId = 10L;

        Transaction transaction = new Transaction();
        User user = new User();
        user.setId(userId);
        transaction.setUser(user);

        TransactionUpdateDTO dto = new TransactionUpdateDTO();
        dto.setAccountId(1L);

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> transactionService.update(transactionId, dto, userId));
    }

    @Test
    void whenUpdate_categoryNotFound_thenThrow() {
        Long transactionId = 1L;
        Long userId = 10L;

        Transaction transaction = new Transaction();
        User user = new User();
        user.setId(userId);
        transaction.setUser(user);

        TransactionUpdateDTO dto = new TransactionUpdateDTO();
        dto.setCategoryId(2L);
        dto.setAccountId(null);

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> transactionService.update(transactionId, dto, userId));
    }


    // === delete ===

    @Test
    void whenDelete_withValidInput_thenDeleteTransaction() {
        Long transactionId = 1L;
        Long userId = 10L;

        Transaction transaction = new Transaction();
        User user = new User();
        user.setId(userId);
        transaction.setUser(user);

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        transactionService.delete(transactionId, userId);

        verify(transactionRepository).delete(transaction);
    }

    @Test
    void whenDelete_transactionNotFound_thenThrow() {
        when(transactionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> transactionService.delete(1L, 10L));
    }

    @Test
    void whenDelete_wrongUser_thenThrow() {
        Transaction transaction = new Transaction();

        User user = new User();
        user.setId(99L);  // wrong user id
        transaction.setUser(user);

        when(transactionRepository.findById(anyLong())).thenReturn(Optional.of(transaction));

        assertThrows(NotFoundException.class, () -> transactionService.delete(1L, 10L));
    }

    @Test
    void whenCreate_withNullCategoryId_thenSaveTransaction() {
        TransactionCreateDTO dto = new TransactionCreateDTO();
        dto.setAccountId(1L);
        dto.setUserId(10L);
        dto.setCategoryId(null);  // <== важливо

        Account account = new Account();
        account.setId(1L);

        User user = new User();
        user.setId(10L);

        Transaction transaction = new Transaction();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(userRepository.findById(10L)).thenReturn(Optional.of(user));
        // categoryRepository.findById НЕ викликається, бо categoryId == null

        when(transactionMapper.toEntity(dto, account, null, user)).thenReturn(transaction);
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        when(transactionMapper.toResponse(transaction)).thenReturn(new TransactionResponseDTO());

        TransactionResponseDTO result = transactionService.create(dto);

        assertNotNull(result);
        verify(transactionRepository).save(transaction);
    }

    @Test
    void whenUpdate_withNullCategoryId_thenSaveTransaction() {
        Long transactionId = 1L;
        Long userId = 10L;

        Transaction transaction = new Transaction();
        User user = new User();
        user.setId(userId);
        transaction.setUser(user);

        TransactionUpdateDTO dto = new TransactionUpdateDTO();
        dto.setAccountId(1L);
        dto.setCategoryId(null);  // <== важливо

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(new Account()));
        // categoryRepository.findById НЕ викликається, бо categoryId == null

        // Тут ти в своєму маппері напевно маєш метод updateTransactionFromDto(transaction, dto, account, category)
        // і передаєш category = null
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        when(transactionMapper.toResponse(transaction)).thenReturn(new TransactionResponseDTO());

        TransactionResponseDTO result = transactionService.update(transactionId, dto, userId);

        assertNotNull(result);
        verify(transactionRepository).save(transaction);
    }


}

