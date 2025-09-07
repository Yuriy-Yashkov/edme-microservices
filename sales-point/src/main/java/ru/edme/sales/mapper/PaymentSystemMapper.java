package ru.edme.sales.mapper;

import org.mapstruct.Mapper;
import ru.edme.sales.dto.requestDto.PaymentSystemRequestDto;
import ru.edme.sales.dto.responseDto.PaymentSystemResponseDto;
import ru.edme.sales.model.PaymentSystem;

@Mapper(componentModel = "spring")
public interface PaymentSystemMapper {

    PaymentSystem toPaymentSystem(PaymentSystemRequestDto paymentSystemRequestDTO);

    PaymentSystem toPaymentSystem(PaymentSystemResponseDto paymentSystemResponseDTO);

    PaymentSystemResponseDto toPaymentSystemResponseDto(PaymentSystem paymentSystem);

    PaymentSystemRequestDto toPaymentSystemRequestDto(PaymentSystemResponseDto paymentSystemResponseDTO);
}
