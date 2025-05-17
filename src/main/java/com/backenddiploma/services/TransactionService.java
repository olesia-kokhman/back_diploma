package com.backenddiploma.services;

import com.backenddiploma.dto.transaction.TransactionCreateDTO;
import com.backenddiploma.dto.transaction.TransactionResponseDTO;
import com.backenddiploma.dto.transaction.TransactionUpdateDTO;
import com.backenddiploma.config.exceptions.NotFoundException;
import com.backenddiploma.mappers.TransactionMapper;
import com.backenddiploma.models.Account;
import com.backenddiploma.models.Category;
import com.backenddiploma.models.Transaction;
import com.backenddiploma.models.User;
import com.backenddiploma.repositories.AccountRepository;
import com.backenddiploma.repositories.CategoryRepository;
import com.backenddiploma.repositories.TransactionRepository;
import com.backenddiploma.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<TransactionResponseDTO> getFilteredAndSortedTransactions(
            Long userId,
            List<Long> categoryIds,
            List<Long> accountIds,
            LocalDate startDate,
            LocalDate endDate,
            String keyword,
            String sortBy,
            boolean direction,
            int page,
            int size
    ) {
        Sort sort = direction ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Transaction> spec = Specification.where(TransactionSpecification.filterByUserId(userId));

        spec = spec.and(TransactionSpecification.filterByCategoryIds(categoryIds));
        spec = spec.and(TransactionSpecification.filterByAccountIds(accountIds));
        spec = spec.and(TransactionSpecification.filterByDateBetween(startDate, endDate));
        spec = spec.and(TransactionSpecification.containsKeyword(keyword));

        Page<Transaction> transactions = transactionRepository.findAll(spec, pageable);

        return transactions.map(transactionMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public TransactionResponseDTO getById(Long id, Long userId) {
        Transaction transaction = transactionRepository.findById(id)
                .filter(t -> t.getUser() != null && t.getUser().getId().equals(userId))
                .orElseThrow(() -> new NotFoundException("Transaction not found with id: " + id + " for user id: " + userId));
        return transactionMapper.toResponse(transaction);
    }

    @Transactional
    public TransactionResponseDTO create(TransactionCreateDTO dto, Long userId) {
        Account account = accountRepository.findById(dto.getAccountId())
                .orElseThrow(() -> new NotFoundException("Account not found with id: " + dto.getAccountId()));

        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found with id: " + dto.getCategoryId()));
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        Transaction transaction = transactionMapper.toEntity(dto, account, category, user);
        Transaction savedTransaction = transactionRepository.save(transaction);

        return transactionMapper.toResponse(savedTransaction);
    }

    @Transactional
    public TransactionResponseDTO update(Long id, TransactionUpdateDTO dto, Long userId) {
        Transaction transaction = transactionRepository.findById(id)
                .filter(t -> t.getUser() != null && t.getUser().getId().equals(userId))
                .orElseThrow(() -> new NotFoundException("Transaction not found with id: " + id + " for user id: " + userId));

        Account account = null;
        if (dto.getAccountId() != null) {
            account = accountRepository.findById(dto.getAccountId())
                    .orElseThrow(() -> new NotFoundException("Account not found with id: " + dto.getAccountId()));
        }

        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found with id: " + dto.getCategoryId()));
        }

        transactionMapper.updateTransactionFromDto(transaction, dto, account, category);
        Transaction updatedTransaction = transactionRepository.save(transaction);

        return transactionMapper.toResponse(updatedTransaction);
    }

    @Transactional
    public void delete(Long id, Long userId) {
        Transaction transaction = transactionRepository.findById(id)
                .filter(t -> t.getUser() != null && t.getUser().getId().equals(userId))
                .orElseThrow(() -> new NotFoundException("Transaction not found with id: " + id + " for user id: " + userId));
        transactionRepository.delete(transaction);
    }
}
