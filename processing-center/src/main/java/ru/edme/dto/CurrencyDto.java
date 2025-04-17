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
public class CurrencyDto {

    private long id;

    @Size(max = 3, min = 3)
    private String currencyDigitalCode;

    @Size(max = 3, min = 3)
    private String currencyLetterCode;

    @Size(max = 255)
    private String currencyName;
}
