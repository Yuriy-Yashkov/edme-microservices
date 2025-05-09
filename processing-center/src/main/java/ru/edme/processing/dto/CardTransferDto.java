package ru.edme.processing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardTransferDto {

    private String cardNumber;
    private LocalDate expirationDate;
    private String holderName;

    private Long cardStatusId; // используется в processing-center
    private Long paymentSystemId; // используется во всех сервисах
    private Long accountId; // используется в processing-center

    private LocalDateTime receivedFromIssuingBank;
    private LocalDateTime sentToIssuingBank;
}
