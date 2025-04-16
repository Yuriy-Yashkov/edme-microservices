package ru.edme.dto.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardResponseDto {

    private long id;
    private String cardNumber;
    private LocalDate expirationDate;
    private String holderName;
    private PaymentSystemResponseDto paymentSystemResponseDTO;
}
