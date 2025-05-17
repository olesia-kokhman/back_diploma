package com.backenddiploma.services;

import com.backenddiploma.dto.savingtip.SavingTipCreateDTO;
import com.backenddiploma.dto.savingtip.SavingTipResponseDTO;
import com.backenddiploma.dto.savingtip.SavingTipUpdateDTO;
import com.backenddiploma.mappers.SavingTipMapper;
import com.backenddiploma.models.SavingTip;
import com.backenddiploma.repositories.SavingTipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.backenddiploma.config.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SavingTipService {

    private final SavingTipRepository savingTipRepository;
    private final SavingTipMapper savingTipMapper;

    @Transactional
    public SavingTipResponseDTO create(SavingTipCreateDTO dto) {
        SavingTip tip = savingTipMapper.toEntity(dto);
        SavingTip savedTip = savingTipRepository.save(tip);
        return savingTipMapper.toResponse(savedTip);
    }

    @Transactional(readOnly = true)
    public SavingTipResponseDTO getById(Long id) {
        SavingTip tip = savingTipRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Saving tip not found with id: " + id));
        return savingTipMapper.toResponse(tip);
    }

    @Transactional
    public SavingTipResponseDTO update(Long id, SavingTipUpdateDTO dto) {
        SavingTip tip = savingTipRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Saving tip not found with id: " + id));
        savingTipMapper.updateSavingTipFromDto(tip, dto);
        SavingTip updatedTip = savingTipRepository.save(tip);
        return savingTipMapper.toResponse(updatedTip);
    }

    @Transactional
    public void delete(Long id) {
        SavingTip tip = savingTipRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Saving tip not found with id: " + id));
        savingTipRepository.delete(tip);
    }

    @Transactional(readOnly = true)
    public List<SavingTipResponseDTO> getAll() {
        return savingTipRepository.findAll().stream()
                .map(savingTipMapper::toResponse)
                .collect(Collectors.toList());
    }
}
