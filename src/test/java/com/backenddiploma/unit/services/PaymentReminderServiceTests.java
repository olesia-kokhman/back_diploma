package com.backenddiploma.unit.services;

import com.backenddiploma.config.exceptions.NotFoundException;
import com.backenddiploma.dto.paymentreminder.PaymentReminderCreateDTO;
import com.backenddiploma.dto.paymentreminder.PaymentReminderResponseDTO;
import com.backenddiploma.dto.paymentreminder.PaymentReminderUpdateDTO;
import com.backenddiploma.dto.savingtip.SavingTipCreateDTO;
import com.backenddiploma.dto.savingtip.SavingTipResponseDTO;
import com.backenddiploma.dto.savingtip.SavingTipUpdateDTO;
import com.backenddiploma.mappers.PaymentReminderMapper;
import com.backenddiploma.mappers.SavingTipMapper;
import com.backenddiploma.models.PaymentReminder;
import com.backenddiploma.models.SavingTip;
import com.backenddiploma.models.User;
import com.backenddiploma.repositories.PaymentReminderRepository;
import com.backenddiploma.repositories.SavingTipRepository;
import com.backenddiploma.repositories.UserRepository;
import com.backenddiploma.services.PaymentReminderService;
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
class PaymentReminderServiceTests {

    @InjectMocks
    private PaymentReminderService paymentReminderService;

    @Mock
    private PaymentReminderRepository paymentReminderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PaymentReminderMapper paymentReminderMapper;

    // === getNearest ===

    @Test
    void whenGetNearest_thenReturnReminder() {
        PaymentReminder reminder = new PaymentReminder();

        when(paymentReminderRepository.findTopByUserIdOrderByDueDateAsc(1L)).thenReturn(reminder);
        when(paymentReminderMapper.toResponse(reminder)).thenReturn(new PaymentReminderResponseDTO());

        PaymentReminderResponseDTO result = paymentReminderService.getNearest(1L);

        assertNotNull(result);
    }

    // === create ===

    @Test
    void whenCreateReminder_thenSaveAndReturn() {
        PaymentReminderCreateDTO dto = new PaymentReminderCreateDTO();
        dto.setUserId(1L);

        User user = new User();
        user.setId(1L);

        PaymentReminder reminder = new PaymentReminder();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(paymentReminderMapper.toEntity(dto, user)).thenReturn(reminder);
        when(paymentReminderRepository.save(reminder)).thenReturn(reminder);
        when(paymentReminderMapper.toResponse(reminder)).thenReturn(new PaymentReminderResponseDTO());

        PaymentReminderResponseDTO result = paymentReminderService.create(dto);

        assertNotNull(result);
        verify(paymentReminderRepository).save(reminder);
    }

    @Test
    void whenCreateReminder_userNotFound_thenThrow() {
        PaymentReminderCreateDTO dto = new PaymentReminderCreateDTO();
        dto.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> paymentReminderService.create(dto));
    }

    // === update ===

    @Test
    void whenUpdateReminder_thenSaveAndReturn() {
        PaymentReminder reminder = new PaymentReminder();
        PaymentReminderUpdateDTO dto = new PaymentReminderUpdateDTO();

        when(paymentReminderRepository.findById(1L)).thenReturn(Optional.of(reminder));
        when(paymentReminderRepository.save(reminder)).thenReturn(reminder);
        when(paymentReminderMapper.toResponse(reminder)).thenReturn(new PaymentReminderResponseDTO());

        PaymentReminderResponseDTO result = paymentReminderService.update(1L, dto);

        assertNotNull(result);
        verify(paymentReminderMapper).updateReminderFromDto(reminder, dto);
        verify(paymentReminderRepository).save(reminder);
    }

    @Test
    void whenUpdateReminder_notFound_thenThrow() {
        when(paymentReminderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> paymentReminderService.update(1L, new PaymentReminderUpdateDTO()));
    }

    // === getById ===

    @Test
    void whenGetById_thenReturnReminder() {
        PaymentReminder reminder = new PaymentReminder();

        when(paymentReminderRepository.findById(1L)).thenReturn(Optional.of(reminder));
        when(paymentReminderMapper.toResponse(reminder)).thenReturn(new PaymentReminderResponseDTO());

        PaymentReminderResponseDTO result = paymentReminderService.getById(1L);

        assertNotNull(result);
    }

    @Test
    void whenGetById_notFound_thenThrow() {
        when(paymentReminderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> paymentReminderService.getById(1L));
    }

    // === getByUserId ===

    @Test
    void whenGetByUserId_thenReturnList() {
        PaymentReminder reminder1 = new PaymentReminder();
        PaymentReminder reminder2 = new PaymentReminder();

        when(paymentReminderRepository.findByUserId(1L)).thenReturn(List.of(reminder1, reminder2));
        when(paymentReminderMapper.toResponse(reminder1)).thenReturn(new PaymentReminderResponseDTO());
        when(paymentReminderMapper.toResponse(reminder2)).thenReturn(new PaymentReminderResponseDTO());

        List<PaymentReminderResponseDTO> result = paymentReminderService.getByUserId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    // === delete ===

    @Test
    void whenDeleteReminder_thenDelete() {
        when(paymentReminderRepository.existsById(1L)).thenReturn(true);

        paymentReminderService.delete(1L);

        verify(paymentReminderRepository).deleteById(1L);
    }

    @Test
    void whenDeleteReminder_notFound_thenThrow() {
        when(paymentReminderRepository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> paymentReminderService.delete(1L));
    }
}
