package com.backenddiploma.services;

import com.backenddiploma.models.Transaction;
import com.backenddiploma.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}
