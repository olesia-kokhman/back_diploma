package com.backenddiploma.repositories;

import com.backenddiploma.models.SavingTip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingTipRepository extends JpaRepository<SavingTip, Long> {
}
