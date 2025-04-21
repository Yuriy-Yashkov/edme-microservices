package ru.edme.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

    private long id;
    private LocalDate transactionDate;
    private BigDecimal sum;

    @Size(max = 255)
    private String transactionName;
    private TransactionTypeDto transactionType;
    private AccountDto account;
    private LocalDateTime sentToProcessingCenter;
    private LocalDateTime receivedFromProcessingCenter;
}
