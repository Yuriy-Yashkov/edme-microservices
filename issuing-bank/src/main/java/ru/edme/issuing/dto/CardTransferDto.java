package ru.edme.issuing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardTransferDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String cardNumber;
    private LocalDate expirationDate;
    private String holderName;

    private Long cardStatusId; // используется в processing-center
    private Long paymentSystemId; // используется во всех сервисах
    private Long accountId; // используется в processing-center

    private LocalDateTime sentToProcessingCenter;
    private Long issuingBankId;
}
