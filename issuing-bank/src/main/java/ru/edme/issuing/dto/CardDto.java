package ru.edme.issuing.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {

    private long id;

    @Size(max = 50)
    private String cardNumber;
    private LocalDate expirationDate;

    @Size(max = 50)
    private String holderName;
    private CardStatusDto cardStatus;
    private PaymentSystemDto paymentSystem;
    private AccountDto account;
    private ClientDto client;
    private LocalDateTime sentToProcessingCenter;
    private LocalDateTime receivedFromProcessingCenter;
}
