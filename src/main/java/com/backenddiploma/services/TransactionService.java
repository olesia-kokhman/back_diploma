package com.backenddiploma.services;

import com.backenddiploma.logic.TransactionFilterSpecification;
import com.backenddiploma.logic.TransactionSearchSpecification;
import com.backenddiploma.models.Transaction;
import com.backenddiploma.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public Transaction addTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction updateTransaction(Long id, Transaction transaction) {
        if(transactionRepository.existsById(id)) {
            transaction.setId(id);
            return transactionRepository.save(transaction);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void deleteTransaction(Long id) {
        if(transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Page<Transaction> getFilteredAndSortedTransactions(
            List<Long> categoryIds,
            List<Long> accountIds,
            LocalDate startDate,
            LocalDate endDate,
            String keyword,
            String sortBy,
            boolean direction,
            int page,
            int size) {

        Specification<Transaction> spec = Specification.where(TransactionFilterSpecification.filterByCategoryIds(categoryIds))
                .and(TransactionFilterSpecification.filterByAccountIds(accountIds))
                .and(TransactionFilterSpecification.filterByDateBetween(startDate, endDate))
                .and(TransactionSearchSpecification.containsKeyword(keyword));

        Sort.Direction sortDirection = direction ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDirection, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return transactionRepository.findAll(spec, pageable);
    }

}
