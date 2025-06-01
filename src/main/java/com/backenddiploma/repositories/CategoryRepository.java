package com.backenddiploma.repositories;

import com.backenddiploma.models.Category;
import com.backenddiploma.models.enums.BudgetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByUserId(Long userId);
    @Query("SELECT c FROM Category c WHERE c.user.id = :userId AND c.startMcc <= :mcc AND c.endMcc >= :mcc ORDER BY c.startMcc ASC")
    Optional<Category> findByMccRangeAndUserId(@Param("mcc") int mcc, @Param("userId") Long userId);

    Optional<Category> findByNameAndUserIdAndType(String name, Long userId, BudgetType type);



}
