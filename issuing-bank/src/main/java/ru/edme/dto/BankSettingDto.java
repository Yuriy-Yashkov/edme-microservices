package ru.edme.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankSettingDto {

    private long id;

    @Size(max = 100)
    private String setting;

    @Size(max = 255)
    private String currentValue;

    @Size(max = 255)
    private String description;
}
