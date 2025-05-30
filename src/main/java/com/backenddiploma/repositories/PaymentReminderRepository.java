package com.backenddiploma.repositories;

import com.backenddiploma.models.PaymentReminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentReminderRepository extends JpaRepository<PaymentReminder, Long> {

    List<PaymentReminder> findByUserId(Long userId);

    @Query("SELECT r FROM PaymentReminder r WHERE r.user.id = :userId AND r.dueDate >= CURRENT_DATE ORDER BY r.dueDate ASC LIMIT 1")
    PaymentReminder findTopByUserIdOrderByDueDateAsc(@Param("userId") Long userId);

}
