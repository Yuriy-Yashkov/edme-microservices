package ru.edme.sales.dto.requestDto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSystemRequestDto {

    private long id;

    @Size(max = 50)
    private String paymentSystemName;
}
