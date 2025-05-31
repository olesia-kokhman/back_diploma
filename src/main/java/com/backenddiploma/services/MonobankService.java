package com.backenddiploma.services;

import com.backenddiploma.config.exceptions.NotFoundException;
import com.backenddiploma.dto.account.AccountCreateDTO;
import com.backenddiploma.dto.integrations.monobank.MonobankAccountDTO;
import com.backenddiploma.dto.integrations.monobank.MonobankTransactionDTO;
import com.backenddiploma.dto.integrations.monobank.MonobankUserInfoDTO;
import com.backenddiploma.dto.transaction.TransactionCreateDTO;
import com.backenddiploma.mappers.AccountMapper;
import com.backenddiploma.mappers.TransactionMapper;
import com.backenddiploma.mappers.MonobankMapper;
import com.backenddiploma.models.accounts.Account;
import com.backenddiploma.models.Category;
import com.backenddiploma.models.Transaction;
import com.backenddiploma.models.User;
import com.backenddiploma.repositories.AccountRepository;
import com.backenddiploma.repositories.CategoryRepository;
import com.backenddiploma.repositories.TransactionRepository;
import com.backenddiploma.repositories.UserRepository;
import com.backenddiploma.services.integrations.MonobankSyncAccountsQueue;
import com.backenddiploma.services.integrations.MonobankSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MonobankService {

    private final MonobankSyncService monobankSyncService;
    private final MonobankMapper monobankMapper;

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final AccountMapper accountMapper;
    private final TransactionMapper transactionMapper;

    private final MonobankSyncAccountsQueue monobankSyncTransactionsQueue;

    @Transactional
    public void syncData(String token, Long userId) {
        MonobankUserInfoDTO userInfoDTO = monobankSyncService.getUserInfo(token).block();

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        List<MonobankAccountDTO> accounts = userInfoDTO != null ? userInfoDTO.getAccounts() : null;

        if (accounts != null) {
            for (MonobankAccountDTO monobankAccountDTO : accounts) {

                AccountCreateDTO accountDTO = monobankMapper.convertAccountToAccountCreateDTO(monobankAccountDTO, user.getId());
                Account account = accountMapper.toEntity(accountDTO, user);
                accountRepository.save(account);


                monobankSyncTransactionsQueue.enqueue(token, userId, monobankAccountDTO.getId());
            }

            System.out.println("Enqueued " + accounts.size() + " Monobank sync tasks for userId = " + userId);
        }
    }

    @Transactional
    public void syncAccountTransactions(String token, Long userId, String externalAccountId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        Account account = accountRepository.findByUserIdAndExternalAccountId(userId, externalAccountId)
                .orElseThrow(() -> new NotFoundException("Account not found for accountId = " + externalAccountId));

        List<MonobankTransactionDTO> transactions = monobankSyncService
                .getTransactions(token, externalAccountId, "1746057600")
                .block();

        if (transactions != null) {
            for (MonobankTransactionDTO txDTO : transactions) {
                Long transactionCategoryIfNull = txDTO.getBalance() > 0 ? 29L : 28L;

                Category category = categoryRepository
                        .findByMccAndUserId(txDTO.getMcc(), user.getId())
                        .orElseGet(() -> categoryRepository.findById(transactionCategoryIfNull)
                                .orElseThrow(() -> new NotFoundException("User not found")));

                TransactionCreateDTO txCreateDTO = monobankMapper.convertToTransactionCreateDTO(txDTO, user.getId(), account.getId(), category.getId());
                Transaction transaction = transactionMapper.toEntity(txCreateDTO, account, category, user);
                transactionRepository.save(transaction);
            }
        }
    }
}
