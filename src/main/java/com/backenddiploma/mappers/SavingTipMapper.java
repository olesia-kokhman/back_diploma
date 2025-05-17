package com.backenddiploma.mappers;

import com.backenddiploma.dto.savingtip.SavingTipCreateDTO;
import com.backenddiploma.dto.savingtip.SavingTipResponseDTO;
import com.backenddiploma.dto.savingtip.SavingTipUpdateDTO;
import com.backenddiploma.models.SavingTip;
import org.springframework.stereotype.Component;

@Component
public class SavingTipMapper {

    public SavingTip toEntity(SavingTipCreateDTO dto) {
        SavingTip tip = new SavingTip();
        tip.setDescription(dto.getDescription());
        return tip;
    }

    public void updateSavingTipFromDto(SavingTip tip, SavingTipUpdateDTO dto) {
        if (dto.getDescription() != null) {
            tip.setDescription(dto.getDescription());
        }
    }

    public SavingTipResponseDTO toResponse(SavingTip tip) {
        SavingTipResponseDTO response = new SavingTipResponseDTO();
        response.setId(tip.getId());
        response.setDescription(tip.getDescription());
        response.setCreatedAt(tip.getCreatedAt());
        response.setUpdatedAt(tip.getUpdatedAt());
        return response;
    }
}
