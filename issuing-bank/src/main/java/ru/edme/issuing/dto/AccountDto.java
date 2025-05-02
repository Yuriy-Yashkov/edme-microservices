package ru.edme.issuing.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    private AccountTypeDto accountType;
    private ClientDto client;
    private LocalDate accountOpeningDate;
    private boolean suspendingOperations;
    private LocalDate accountClosingDate;
}
