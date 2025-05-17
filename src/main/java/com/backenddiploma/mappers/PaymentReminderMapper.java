package com.backenddiploma.mappers;

import com.backenddiploma.dto.paymentreminder.PaymentReminderCreateDTO;
import com.backenddiploma.dto.paymentreminder.PaymentReminderResponseDTO;
import com.backenddiploma.dto.paymentreminder.PaymentReminderUpdateDTO;
import com.backenddiploma.models.PaymentReminder;
import com.backenddiploma.models.User;
import org.springframework.stereotype.Component;

import com.backenddiploma.models.enums.Currency;

@Component
public class PaymentReminderMapper {

    public PaymentReminder toEntity(PaymentReminderCreateDTO dto, User user) {
        PaymentReminder reminder = new PaymentReminder();
        reminder.setUser(user);
        reminder.setTitle(dto.getTitle());
        reminder.setAmount(dto.getAmount());
        reminder.setCurrency(Currency.valueOf(dto.getCurrency()));
        reminder.setDueDate(dto.getDueDate());
        reminder.setDescription(dto.getDescription());
        return reminder;
    }

    public void updateReminderFromDto(PaymentReminder reminder, PaymentReminderUpdateDTO dto) {
        if (dto.getTitle() != null) {
            reminder.setTitle(dto.getTitle());
        }
        if (dto.getAmount() != null) {
            reminder.setAmount(dto.getAmount());
        }
        if (dto.getCurrency() != null) {
            reminder.setCurrency(Currency.valueOf(dto.getCurrency()));
        }
        if (dto.getDueDate() != null) {
            reminder.setDueDate(dto.getDueDate());
        }
        if (dto.getDescription() != null) {
            reminder.setDescription(dto.getDescription());
        }
    }

    public PaymentReminderResponseDTO toResponse(PaymentReminder reminder) {
        PaymentReminderResponseDTO response = new PaymentReminderResponseDTO();
        response.setId(reminder.getId());
        response.setUserId(reminder.getUser().getId());
        response.setTitle(reminder.getTitle());
        response.setAmount(reminder.getAmount());
        response.setCurrency(reminder.getCurrency().toString());
        response.setDueDate(reminder.getDueDate());
        response.setDescription(reminder.getDescription());
        response.setCreatedAt(reminder.getCreatedAt());
        response.setUpdatedAt(reminder.getUpdatedAt());
        return response;
    }
}
