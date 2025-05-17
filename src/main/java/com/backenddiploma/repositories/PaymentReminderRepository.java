package com.backenddiploma.repositories;

import com.backenddiploma.models.PaymentReminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentReminderRepository extends JpaRepository<PaymentReminder, Long> {

    List<PaymentReminder> findByUserId(Long userId);
}
