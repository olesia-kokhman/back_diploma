package com.backenddiploma.unit.services;

import com.backenddiploma.config.exceptions.NotFoundException;
import com.backenddiploma.dto.savingtip.SavingTipCreateDTO;
import com.backenddiploma.dto.savingtip.SavingTipResponseDTO;
import com.backenddiploma.dto.savingtip.SavingTipUpdateDTO;
import com.backenddiploma.mappers.SavingTipMapper;
import com.backenddiploma.models.SavingTip;
import com.backenddiploma.repositories.SavingTipRepository;
import com.backenddiploma.services.SavingTipService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SavingTipServiceTest {

    @InjectMocks
    private SavingTipService savingTipService;

    @Mock
    private SavingTipRepository savingTipRepository;

    @Mock
    private SavingTipMapper savingTipMapper;

    // === getRandomTip ===

    @Test
    void whenGetRandomTip_thenReturnRandomTip() {
        SavingTip tip1 = new SavingTip();
        SavingTip tip2 = new SavingTip();

        when(savingTipRepository.findAll()).thenReturn(List.of(tip1, tip2));
        when(savingTipMapper.toResponse(any())).thenReturn(new SavingTipResponseDTO());

        SavingTipResponseDTO result = savingTipService.getRandomTip();

        assertNotNull(result);
    }

    @Test
    void whenGetRandomTip_empty_thenThrow() {
        when(savingTipRepository.findAll()).thenReturn(List.of());

        assertThrows(NotFoundException.class, () -> savingTipService.getRandomTip());
    }

    // === create ===

    @Test
    void whenCreate_thenSaveAndReturn() {
        SavingTipCreateDTO dto = new SavingTipCreateDTO();
        SavingTip tip = new SavingTip();

        when(savingTipMapper.toEntity(dto)).thenReturn(tip);
        when(savingTipRepository.save(tip)).thenReturn(tip);
        when(savingTipMapper.toResponse(tip)).thenReturn(new SavingTipResponseDTO());

        SavingTipResponseDTO result = savingTipService.create(dto);

        assertNotNull(result);
        verify(savingTipRepository).save(tip);
    }

    // === getById ===

    @Test
    void whenGetById_thenReturnTip() {
        SavingTip tip = new SavingTip();

        when(savingTipRepository.findById(1L)).thenReturn(Optional.of(tip));
        when(savingTipMapper.toResponse(tip)).thenReturn(new SavingTipResponseDTO());

        SavingTipResponseDTO result = savingTipService.getById(1L);

        assertNotNull(result);
    }

    @Test
    void whenGetById_notFound_thenThrow() {
        when(savingTipRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> savingTipService.getById(1L));
    }

    // === update ===

    @Test
    void whenUpdate_thenSaveAndReturn() {
        SavingTip tip = new SavingTip();
        SavingTipUpdateDTO dto = new SavingTipUpdateDTO();

        when(savingTipRepository.findById(1L)).thenReturn(Optional.of(tip));
        when(savingTipRepository.save(tip)).thenReturn(tip);
        when(savingTipMapper.toResponse(tip)).thenReturn(new SavingTipResponseDTO());

        SavingTipResponseDTO result = savingTipService.update(1L, dto);

        assertNotNull(result);
        verify(savingTipMapper).updateSavingTipFromDto(tip, dto);
        verify(savingTipRepository).save(tip);
    }

    @Test
    void whenUpdate_notFound_thenThrow() {
        when(savingTipRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> savingTipService.update(1L, new SavingTipUpdateDTO()));
    }

    // === delete ===

    @Test
    void whenDelete_thenDeleteTip() {
        SavingTip tip = new SavingTip();

        when(savingTipRepository.findById(1L)).thenReturn(Optional.of(tip));

        savingTipService.delete(1L);

        verify(savingTipRepository).delete(tip);
    }

    @Test
    void whenDelete_notFound_thenThrow() {
        when(savingTipRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> savingTipService.delete(1L));
    }

    // === getAll ===

    @Test
    void whenGetAll_thenReturnMappedList() {
        SavingTip tip1 = new SavingTip();
        SavingTip tip2 = new SavingTip();

        when(savingTipRepository.findAll()).thenReturn(List.of(tip1, tip2));
        when(savingTipMapper.toResponse(tip1)).thenReturn(new SavingTipResponseDTO());
        when(savingTipMapper.toResponse(tip2)).thenReturn(new SavingTipResponseDTO());

        List<SavingTipResponseDTO> result = savingTipService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
