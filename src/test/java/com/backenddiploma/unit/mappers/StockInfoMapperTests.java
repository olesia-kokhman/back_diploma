package com.backenddiploma.unit.mappers;

import com.backenddiploma.dto.stockinfo.StockInfoCreateDTO;
import com.backenddiploma.dto.stockinfo.StockInfoResponseDTO;
import com.backenddiploma.mappers.StockInfoMapper;
import com.backenddiploma.models.StockInfo;
import com.backenddiploma.models.enums.StockTrend;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

class StockInfoMapperTest {

    private StockInfoMapper stockInfoMapper;

    @BeforeEach
    void setUp() {
        stockInfoMapper = new StockInfoMapper();
    }

    @Test
    void testToEntity() {
        StockInfoCreateDTO dto = new StockInfoCreateDTO();
        dto.setSymbol("AAPL");
        dto.setCurrentPrice(180.25);
        dto.setPercentChange(1.5);
        dto.setTimestamp(Instant.now().getEpochSecond());

        StockTrend trend = StockTrend.UP;

        StockInfo stockInfo = stockInfoMapper.toEntity(dto, trend);

        assertThat(stockInfo.getSymbol()).isEqualTo("AAPL");
        assertThat(stockInfo.getName()).isEqualTo("Default name, needs another API to change");
        assertThat(stockInfo.getCurrentPrice()).isEqualTo(180.25);
        assertThat(stockInfo.getChangePercent()).isEqualTo(1.5);

        LocalDateTime expectedDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(dto.getTimestamp()), ZoneId.systemDefault());
        assertThat(stockInfo.getGeneratedAt()).isEqualTo(expectedDate);

        assertThat(stockInfo.getTrend()).isEqualTo(StockTrend.UP);
    }

    @Test
    void testToResponse() {
        StockInfo stockInfo = new StockInfo();
        stockInfo.setId(10L);
        stockInfo.setSymbol("TSLA");
        stockInfo.setName("Tesla Inc.");
        stockInfo.setCurrentPrice(750.0);
        stockInfo.setChangePercent(-2.3);

        LocalDateTime generatedAt = LocalDateTime.of(2025, 6, 1, 15, 0);
        stockInfo.setGeneratedAt(generatedAt);
        stockInfo.setTrend(StockTrend.DOWN);

        LocalDateTime createdAt = LocalDateTime.of(2025, 6, 1, 12, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2025, 6, 2, 16, 30);
        stockInfo.setCreatedAt(createdAt);
        stockInfo.setUpdatedAt(updatedAt);

        StockInfoResponseDTO response = stockInfoMapper.toResponse(stockInfo);

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getSymbol()).isEqualTo("TSLA");
        assertThat(response.getName()).isEqualTo("Tesla Inc.");
        assertThat(response.getCurrentPrice()).isEqualTo(750.0);
        assertThat(response.getChangePercent()).isEqualTo(-2.3);
        assertThat(response.getGeneratedAt()).isEqualTo(generatedAt);
        assertThat(response.getTrend()).isEqualTo(StockTrend.DOWN);
        assertThat(response.getCreatedAt()).isEqualTo(createdAt);
        assertThat(response.getUpdatedAt()).isEqualTo(updatedAt);
    }
}
