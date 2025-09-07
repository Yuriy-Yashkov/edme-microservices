package ru.edme.processing.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private long id;

    @Size(max = 50)
    private String accountNumber;
    private BigDecimal balance;
    private CurrencyDto currency;
    private IssuingBankDto issuingBank;
}
