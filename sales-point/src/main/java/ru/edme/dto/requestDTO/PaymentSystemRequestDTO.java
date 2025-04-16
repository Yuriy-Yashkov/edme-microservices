package ru.edme.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSystemRequestDTO {

    private long id;
    private String paymentSystemName;
}
