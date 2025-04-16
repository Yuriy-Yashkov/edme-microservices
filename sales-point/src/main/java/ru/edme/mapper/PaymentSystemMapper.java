package ru.edme.mapper;

import org.mapstruct.Mapper;
import ru.edme.dto.requestDto.PaymentSystemRequestDto;
import ru.edme.dto.responseDto.PaymentSystemResponseDto;
import ru.edme.model.PaymentSystem;

@Mapper(componentModel = "spring")
public interface PaymentSystemMapper {

    PaymentSystem toPaymentSystem(PaymentSystemRequestDto paymentSystemRequestDTO);

    PaymentSystem toPaymentSystem(PaymentSystemResponseDto paymentSystemResponseDTO);

    PaymentSystemResponseDto toPaymentSystemResponseDto(PaymentSystem paymentSystem);

    PaymentSystemRequestDto toPaymentSystemRequestDto(PaymentSystemResponseDto paymentSystemResponseDTO);
}
