package com.backenddiploma.unit.mappers;

import com.backenddiploma.dto.savingtip.SavingTipCreateDTO;
import com.backenddiploma.dto.savingtip.SavingTipResponseDTO;
import com.backenddiploma.dto.savingtip.SavingTipUpdateDTO;
import com.backenddiploma.mappers.SavingTipMapper;
import com.backenddiploma.models.SavingTip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class SavingTipMapperTest {

    private SavingTipMapper savingTipMapper;

    @BeforeEach
    void setUp() {
        savingTipMapper = new SavingTipMapper();
    }

    @Test
    void testToEntity() {
        SavingTipCreateDTO dto = new SavingTipCreateDTO();
        dto.setDescription("Save 10% of your salary every month");

        SavingTip tip = savingTipMapper.toEntity(dto);

        assertThat(tip.getDescription()).isEqualTo("Save 10% of your salary every month");
    }

    @Test
    void testUpdateSavingTipFromDto() {
        SavingTip tip = new SavingTip();
        tip.setDescription("Old description");

        SavingTipUpdateDTO dto = new SavingTipUpdateDTO();
        dto.setDescription("Updated tip: track your daily expenses");

        savingTipMapper.updateSavingTipFromDto(tip, dto);

        assertThat(tip.getDescription()).isEqualTo("Updated tip: track your daily expenses");
    }

    @Test
    void testToResponse() {
        SavingTip tip = new SavingTip();
        tip.setId(5L);
        tip.setDescription("Use cashback cards");

        tip.setCreatedAt(LocalDateTime.of(2025, 6, 1, 12, 0));
        tip.setUpdatedAt(LocalDateTime.of(2025, 6, 2, 15, 30));

        SavingTipResponseDTO response = savingTipMapper.toResponse(tip);

        assertThat(response.getId()).isEqualTo(5L);
        assertThat(response.getDescription()).isEqualTo("Use cashback cards");
        assertThat(response.getCreatedAt()).isEqualTo(LocalDateTime.of(2025, 6, 1, 12, 0));
        assertThat(response.getUpdatedAt()).isEqualTo(LocalDateTime.of(2025, 6, 2, 15, 30));
    }
}
