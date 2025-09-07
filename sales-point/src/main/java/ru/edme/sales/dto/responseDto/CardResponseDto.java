package ru.edme.sales.dto.responseDto;

import jakarta.validation.constraints.Size;
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

    @Size(max = 50)
    private String cardNumber;
    private LocalDate expirationDate;

    @Size(max = 50)
    private String holderName;
    private PaymentSystemResponseDto paymentSystem;
}
