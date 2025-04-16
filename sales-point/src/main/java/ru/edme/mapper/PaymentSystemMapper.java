package ru.edme.mapper;

import org.mapstruct.Mapper;
import ru.edme.dto.requestDTO.PaymentSystemRequestDTO;
import ru.edme.dto.responseDTO.PaymentSystemResponseDTO;
import ru.edme.model.PaymentSystem;

@Mapper(componentModel = "spring")
public interface PaymentSystemMapper {

    PaymentSystem toPaymentSystem(PaymentSystemRequestDTO paymentSystemRequestDTO);

    PaymentSystem toPaymentSystem(PaymentSystemResponseDTO paymentSystemResponseDTO);

    PaymentSystemResponseDTO toPaymentSystemResponseDto(PaymentSystem paymentSystem);

    PaymentSystemRequestDTO toPaymentSystemRequestDto(PaymentSystemResponseDTO paymentSystemResponseDTO);
}
