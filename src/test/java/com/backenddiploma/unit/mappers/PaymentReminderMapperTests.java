package com.backenddiploma.unit.mappers;

import com.backenddiploma.dto.paymentreminder.PaymentReminderCreateDTO;
import com.backenddiploma.dto.paymentreminder.PaymentReminderResponseDTO;
import com.backenddiploma.dto.paymentreminder.PaymentReminderUpdateDTO;
import com.backenddiploma.mappers.PaymentReminderMapper;
import com.backenddiploma.models.PaymentReminder;
import com.backenddiploma.models.User;
import com.backenddiploma.models.enums.Currency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentReminderMapperTest {

    private PaymentReminderMapper paymentReminderMapper;

    @BeforeEach
    void setUp() {
        paymentReminderMapper = new PaymentReminderMapper();
    }

    @Test
    void testToEntity() {
        PaymentReminderCreateDTO dto = new PaymentReminderCreateDTO();
        dto.setTitle("Pay Rent");
        dto.setAmount(1000.0);
        dto.setCurrency("USD");
        dto.setDueDate(LocalDate.of(2025, 6, 15));
        dto.setDescription("Monthly rent payment");

        User user = new User();
        user.setId(1L);

        PaymentReminder reminder = paymentReminderMapper.toEntity(dto, user);

        assertThat(reminder.getUser()).isEqualTo(user);
        assertThat(reminder.getTitle()).isEqualTo("Pay Rent");
        assertThat(reminder.getAmount()).isEqualTo(1000.0);
        assertThat(reminder.getCurrency()).isEqualTo(Currency.USD);
        assertThat(reminder.getDueDate()).isEqualTo(LocalDate.of(2025, 6, 15));
        assertThat(reminder.getDescription()).isEqualTo("Monthly rent payment");
    }

    @Test
    void testUpdateReminderFromDto() {
        PaymentReminder reminder = new PaymentReminder();
        reminder.setTitle("Old Title");
        reminder.setAmount(500.0);
        reminder.setCurrency(Currency.UAH);
        reminder.setDueDate(LocalDate.of(2025, 5, 1));
        reminder.setDescription("Old Description");

        PaymentReminderUpdateDTO dto = new PaymentReminderUpdateDTO();
        dto.setTitle("Updated Title");
        dto.setAmount(750.0);
        dto.setCurrency("EUR");
        dto.setDueDate(LocalDate.of(2025, 7, 10));
        dto.setDescription("Updated Description");

        paymentReminderMapper.updateReminderFromDto(reminder, dto);

        assertThat(reminder.getTitle()).isEqualTo("Updated Title");
        assertThat(reminder.getAmount()).isEqualTo(750.0);
        assertThat(reminder.getCurrency()).isEqualTo(Currency.EUR);
        assertThat(reminder.getDueDate()).isEqualTo(LocalDate.of(2025, 7, 10));
        assertThat(reminder.getDescription()).isEqualTo("Updated Description");
    }

    @Test
    void testToResponse() {
        PaymentReminder reminder = new PaymentReminder();
        reminder.setId(5L);

        User user = new User();
        user.setId(10L);
        reminder.setUser(user);

        reminder.setTitle("Car Insurance");
        reminder.setAmount(300.0);
        reminder.setCurrency(Currency.USD);
        reminder.setDueDate(LocalDate.of(2025, 8, 20));
        reminder.setDescription("Annual car insurance payment");

        reminder.setCreatedAt(LocalDateTime.of(2025, 6, 1, 12, 0));
        reminder.setUpdatedAt(LocalDateTime.of(2025, 6, 2, 15, 30));

        PaymentReminderResponseDTO response = paymentReminderMapper.toResponse(reminder);

        assertThat(response.getId()).isEqualTo(5L);
        assertThat(response.getUserId()).isEqualTo(10L);
        assertThat(response.getTitle()).isEqualTo("Car Insurance");
        assertThat(response.getAmount()).isEqualTo(300.0);
        assertThat(response.getCurrency()).isEqualTo("USD");
        assertThat(response.getDueDate()).isEqualTo(LocalDate.of(2025, 8, 20));
        assertThat(response.getDescription()).isEqualTo("Annual car insurance payment");
        assertThat(response.getCreatedAt()).isEqualTo(LocalDateTime.of(2025, 6, 1, 12, 0));
        assertThat(response.getUpdatedAt()).isEqualTo(LocalDateTime.of(2025, 6, 2, 15, 30));
    }
}
