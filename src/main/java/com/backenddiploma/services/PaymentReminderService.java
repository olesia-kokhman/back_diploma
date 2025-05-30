package com.backenddiploma.services;

import com.backenddiploma.dto.paymentreminder.PaymentReminderCreateDTO;
import com.backenddiploma.dto.paymentreminder.PaymentReminderResponseDTO;
import com.backenddiploma.dto.paymentreminder.PaymentReminderUpdateDTO;
import com.backenddiploma.config.exceptions.NotFoundException;
import com.backenddiploma.mappers.PaymentReminderMapper;
import com.backenddiploma.models.PaymentReminder;
import com.backenddiploma.models.User;
import com.backenddiploma.repositories.PaymentReminderRepository;
import com.backenddiploma.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentReminderService {

    private final PaymentReminderRepository paymentReminderRepository;
    private final UserRepository userRepository;
    private final PaymentReminderMapper paymentReminderMapper;

    @Transactional(readOnly = true)
    public PaymentReminderResponseDTO getNearest(Long userId) {
        PaymentReminder reminder = paymentReminderRepository.findTopByUserIdOrderByDueDateAsc(userId);
        return paymentReminderMapper.toResponse(reminder);
    }


    @Transactional
    public PaymentReminderResponseDTO create(PaymentReminderCreateDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        PaymentReminder reminder = paymentReminderMapper.toEntity(dto, user);
        return paymentReminderMapper.toResponse(paymentReminderRepository.save(reminder));
    }

    @Transactional
    public PaymentReminderResponseDTO update(Long id, PaymentReminderUpdateDTO dto) {
        PaymentReminder reminder = paymentReminderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment reminder not found"));
        paymentReminderMapper.updateReminderFromDto(reminder, dto);
        return paymentReminderMapper.toResponse(paymentReminderRepository.save(reminder));
    }

    @Transactional(readOnly = true)
    public PaymentReminderResponseDTO getById(Long id) {
        PaymentReminder reminder = paymentReminderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment reminder not found"));
        return paymentReminderMapper.toResponse(reminder);
    }

    @Transactional(readOnly = true)
    public List<PaymentReminderResponseDTO> getByUserId(Long userId) {
        return paymentReminderRepository.findByUserId(userId).stream()
                .map(paymentReminderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        if (!paymentReminderRepository.existsById(id)) {
            throw new NotFoundException("Payment reminder not found");
        }
        paymentReminderRepository.deleteById(id);
    }
}
