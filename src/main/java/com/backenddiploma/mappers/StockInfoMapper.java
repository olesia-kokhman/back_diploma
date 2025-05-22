package com.backenddiploma.mappers;

import com.backenddiploma.dto.stockinfo.StockInfoCreateDTO;
import com.backenddiploma.dto.stockinfo.StockInfoResponseDTO;
import com.backenddiploma.models.StockInfo;
import com.backenddiploma.models.enums.StockTrend;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class StockInfoMapper {

    public StockInfo toEntity(StockInfoCreateDTO dto, StockTrend trend) {
        StockInfo stockInfo = new StockInfo();
        stockInfo.setSymbol(dto.getSymbol());
        stockInfo.setName("Default name, needs another API to change"); // another API
        stockInfo.setCurrentPrice(dto.getCurrentPrice());
        stockInfo.setChangePercent(dto.getPercentChange());
        stockInfo.setGeneratedAt(LocalDateTime.ofInstant(Instant.ofEpochSecond(dto.getTimestamp()), ZoneId.systemDefault()));
        stockInfo.setTrend(trend);
        return stockInfo;
    }

    public StockInfoResponseDTO toResponse(StockInfo stockInfo) {
        StockInfoResponseDTO dto = new StockInfoResponseDTO();
        dto.setId(stockInfo.getId());
        dto.setSymbol(stockInfo.getSymbol());
        dto.setName(stockInfo.getName());
        dto.setCurrentPrice(stockInfo.getCurrentPrice());
        dto.setChangePercent(stockInfo.getChangePercent());
        dto.setGeneratedAt(stockInfo.getGeneratedAt());
        dto.setTrend(stockInfo.getTrend());
        dto.setCreatedAt(stockInfo.getCreatedAt());
        dto.setUpdatedAt(stockInfo.getUpdatedAt());
        return dto;
    }
}
