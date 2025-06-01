package com.backenddiploma.repositories;

import com.backenddiploma.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    List<Transaction> findAllByUserIdAndTransferredAtBetween(Long userId, LocalDateTime start, LocalDateTime end);

    @Query("""
    SELECT COALESCE(SUM(t.amount), 0)
    FROM Transaction t
    WHERE t.user.id = :userId
      AND t.category.id = :categoryId
      AND t.transferredAt BETWEEN :startDate AND :endDate
    """)
    Double calculateSpentAmount(Long userId, Long categoryId, LocalDateTime startDate, LocalDateTime endDate);

}
