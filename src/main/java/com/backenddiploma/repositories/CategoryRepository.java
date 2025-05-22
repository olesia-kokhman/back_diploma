package com.backenddiploma.repositories;

import com.backenddiploma.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByUserId(Long userId);

    @Query("SELECT c FROM Category c WHERE :mcc BETWEEN c.startMcc AND c.endMcc AND c.user.id = :userId")
    Optional<Category> findByMccAndUserId(@Param("mcc") Integer mcc, @Param("userId") Long userId);

}
