package com.backenddiploma.repositories;

import com.backenddiploma.models.Budget;
import com.backenddiploma.models.Category;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    boolean existsByUserIdAndCategoryIdAndPeriodStart(Long userId, Long categoryId, LocalDate periodStart);
    List<Budget> findAllByUserIdAndPeriodStart(Long userId, LocalDate periodStart);
    boolean existsByUserIdAndPeriodStart(Long userId, LocalDate periodStart);
    Optional<Budget> findByUserIdAndCategoryIdAndPeriodStart(Long userId, Long categoryId, LocalDate periodStart);

    @Query("""
    SELECT b
    FROM Budget b
    WHERE b.user.id = :userId
      AND b.periodStart = :periodStart
      AND b.category.type = 'EXPENSE'
      AND b.category.id <> :categoryId
    """)
    List<Budget> findAllByUserIdAndPeriodStartAndExpenseCategoriesExcludingCategory(Long userId, LocalDate periodStart, Long categoryId );

    @Query("""
    SELECT b
    FROM Budget b
    WHERE b.user.id = :userId
      AND b.periodStart = :periodStart
      AND b.category.type = 'INCOME'
      AND b.category.id <> :categoryId
    """)
    List<Budget> findAllByUserIdAndPeriodStartAndIncomeCategoriesExcludingCategory(Long userId, LocalDate periodStart, Long categoryId);


}
