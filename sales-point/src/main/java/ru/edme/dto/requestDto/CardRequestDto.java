package ru.edme.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardRequestDto {

    private long id;
    private String cardNumber;
    private LocalDate expirationDate;
    private String holderName;
    private PaymentSystemRequestDto paymentSystemRequestDTO;
}
