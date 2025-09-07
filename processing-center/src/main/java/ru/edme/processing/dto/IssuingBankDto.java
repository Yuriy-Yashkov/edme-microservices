package ru.edme.processing.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssuingBankDto {

    private long id;

    @Size(max = 9, min = 9)
    private String bic;

    @Size(max = 5, min = 5)
    private String bin;

    @Size(max = 255)
    private String abbreviatedName;
}
