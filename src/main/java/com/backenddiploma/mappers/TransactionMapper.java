package com.backenddiploma.mappers;

import com.backenddiploma.dto.transaction.TransactionCreateDTO;
import com.backenddiploma.dto.transaction.TransactionResponseDTO;
import com.backenddiploma.dto.transaction.TransactionUpdateDTO;
import com.backenddiploma.models.*;
import com.backenddiploma.models.accounts.Account;
import org.springframework.stereotype.Component;

import com.backenddiploma.models.enums.Currency;

@Component
public class TransactionMapper {

    public Transaction toEntity(TransactionCreateDTO request, Account account, Category category, User user) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(request.getTransactionType());
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(Currency.valueOf(request.getCurrency()));
        transaction.setDescription(request.getDescription());
        transaction.setAccount(account);
        transaction.setCategory(category);
        transaction.setUser(user);
        transaction.setTransferredAt(request.getTransferredAt());
        return transaction;
    }

    public void updateTransactionFromDto(Transaction transaction, TransactionUpdateDTO request, Account account, Category category) {
        if (request.getTransactionType() != null) {
            transaction.setTransactionType(request.getTransactionType());
        }
        if (request.getAmount() != null) {
            transaction.setAmount(request.getAmount());
        }
        if (request.getCurrency() != null) {
            transaction.setCurrency(Currency.valueOf(request.getCurrency()));
        }
        if (request.getDescription() != null) {
            transaction.setDescription(request.getDescription());
        }
        if (account != null) {
            transaction.setAccount(account);
        }
        if (category != null) {
            transaction.setCategory(category);
        }
        if (request.getTransferredAt() != null) {
            transaction.setTransferredAt(request.getTransferredAt());
        }

    }

    public TransactionResponseDTO toResponse(Transaction transaction) {
        TransactionResponseDTO response = new TransactionResponseDTO();
        response.setId(transaction.getId());
        response.setTransactionType(transaction.getTransactionType());
        response.setAmount(transaction.getAmount());
        response.setCurrency(transaction.getCurrency().toString());
        response.setDescription(transaction.getDescription());
        response.setAccountId(transaction.getAccount().getId());
        response.setCategoryId(transaction.getCategory() != null ? transaction.getCategory().getId() : null);
        response.setUserId(transaction.getUser() != null ? transaction.getUser().getId() : null);
        response.setTransferredAt(transaction.getTransferredAt());
        response.setCreatedAt(transaction.getCreatedAt());
        response.setUpdatedAt(transaction.getUpdatedAt());
        return response;
    }
}
