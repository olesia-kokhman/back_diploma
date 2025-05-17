package com.backenddiploma.repositories;

import com.backenddiploma.models.StockInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockInfoRepository extends JpaRepository<StockInfo, String> {

}
