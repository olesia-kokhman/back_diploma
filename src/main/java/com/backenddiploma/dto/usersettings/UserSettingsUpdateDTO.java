package com.backenddiploma.dto.usersettings;

import com.backenddiploma.models.enums.LanguageAbbreviation;
import lombok.Data;

@Data
public class UserSettingsUpdateDTO {

    private LanguageAbbreviation language;
    private String defaultCurrency;
    private String dateFormat;
    private String timeFormat;
}
