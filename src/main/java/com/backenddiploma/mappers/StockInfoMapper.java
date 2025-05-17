package com.backenddiploma.mappers;

import com.backenddiploma.dto.stockinfo.out.StockInfoCreateDTO;
import com.backenddiploma.dto.stockinfo.out.StockInfoResponseDTO;
import com.backenddiploma.dto.stockinfo.out.StockInfoUpdateDTO;
import com.backenddiploma.models.StockInfo;
import org.springframework.stereotype.Component;

@Component
public class StockInfoMapper {

    public StockInfo toEntity(StockInfoCreateDTO dto) {
        StockInfo stockInfo = new StockInfo();
        stockInfo.setSymbol(dto.getSymbol());
        stockInfo.setName(dto.getName());
        stockInfo.setCurrentPrice(dto.getCurrentPrice());
        stockInfo.setChangePercent(dto.getChangePercent());
        stockInfo.setTrend(dto.getTrend());
        return stockInfo;
    }

    public void updateStockInfoFromDto(StockInfo stockInfo, StockInfoUpdateDTO dto) {
        if (dto.getName() != null) {
            stockInfo.setName(dto.getName());
        }
        if (dto.getCurrentPrice() != null) {
            stockInfo.setCurrentPrice(dto.getCurrentPrice());
        }
        if (dto.getChangePercent() != null) {
            stockInfo.setChangePercent(dto.getChangePercent());
        }
        if (dto.getTrend() != null) {
            stockInfo.setTrend(dto.getTrend());
        }
    }

    public StockInfoResponseDTO toResponse(StockInfo stockInfo) {
        StockInfoResponseDTO response = new StockInfoResponseDTO();
        response.setSymbol(stockInfo.getSymbol());
        response.setName(stockInfo.getName());
        response.setCurrentPrice(stockInfo.getCurrentPrice());
        response.setChangePercent(stockInfo.getChangePercent());
        response.setTrend(stockInfo.getTrend());
        response.setCreatedAt(stockInfo.getCreatedAt());
        response.setUpdatedAt(stockInfo.getUpdatedAt());
        return response;
    }
}
