package ru.edme.sales.dto.requestDto;

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
public class CardRequestDto {

    private long id;

    @Size(max = 50)
    private String cardNumber;
    private LocalDate expirationDate;

    @Size(max = 50)
    private String holderName;
    private PaymentSystemRequestDto paymentSystem;
}
