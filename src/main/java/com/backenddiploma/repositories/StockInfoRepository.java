package com.backenddiploma.repositories;

import com.backenddiploma.models.StockInfo;;
import com.backenddiploma.models.enums.StockTrend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockInfoRepository extends JpaRepository<StockInfo, Long> {
    Optional<StockInfo> findBySymbol(String symbol);
    List<StockInfo> findByTrend(StockTrend trend);
}
