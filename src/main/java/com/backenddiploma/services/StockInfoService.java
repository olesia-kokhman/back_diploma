package com.backenddiploma.services;

import com.backenddiploma.config.exceptions.AlreadyExistsException;
import com.backenddiploma.dto.stockinfo.out.StockInfoCreateDTO;
import com.backenddiploma.dto.stockinfo.out.StockInfoResponseDTO;
import com.backenddiploma.dto.stockinfo.out.StockInfoUpdateDTO;
import com.backenddiploma.config.exceptions.NotFoundException;
import com.backenddiploma.mappers.StockInfoMapper;
import com.backenddiploma.models.StockInfo;
import com.backenddiploma.repositories.StockInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockInfoService {

    private final StockInfoRepository stockInfoRepository;
    private final StockInfoMapper stockInfoMapper;

    @Transactional
    public StockInfoResponseDTO create(StockInfoCreateDTO dto) {
        if (stockInfoRepository.existsById(dto.getSymbol())) {
            throw new AlreadyExistsException("Stock with symbol already exists: " + dto.getSymbol());
        }

        StockInfo stockInfo = stockInfoMapper.toEntity(dto);
        StockInfo savedStock = stockInfoRepository.save(stockInfo);

        return stockInfoMapper.toResponse(savedStock);
    }

    @Transactional(readOnly = true)
    public StockInfoResponseDTO getBySymbol(String symbol) {
        StockInfo stockInfo = stockInfoRepository.findById(symbol)
                .orElseThrow(() -> new NotFoundException("Stock not found with symbol: " + symbol));
        return stockInfoMapper.toResponse(stockInfo);
    }

    @Transactional
    public StockInfoResponseDTO update(String symbol, StockInfoUpdateDTO dto) {
        StockInfo stockInfo = stockInfoRepository.findById(symbol)
                .orElseThrow(() -> new NotFoundException("Stock not found with symbol: " + symbol));

        stockInfoMapper.updateStockInfoFromDto(stockInfo, dto);
        StockInfo updatedStock = stockInfoRepository.save(stockInfo);

        return stockInfoMapper.toResponse(updatedStock);
    }

    @Transactional
    public void delete(String symbol) {
        StockInfo stockInfo = stockInfoRepository.findById(symbol)
                .orElseThrow(() -> new NotFoundException("Stock not found with symbol: " + symbol));
        stockInfoRepository.delete(stockInfo);
    }

    @Transactional(readOnly = true)
    public List<StockInfoResponseDTO> getAll() {
        return stockInfoRepository.findAll().stream()
                .map(stockInfoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
