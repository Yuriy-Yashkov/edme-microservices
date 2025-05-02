package ru.edme.processing.mapper;

import org.mapstruct.Mapper;
import ru.edme.processing.dto.PaymentSystemDto;
import ru.edme.processing.model.PaymentSystem;

@Mapper(componentModel = "spring")
public interface PaymentSystemMapper {

    PaymentSystemDto toPaymentSystemDto(PaymentSystem paymentSystem);

    PaymentSystem toPaymentSystem(PaymentSystemDto paymentSystemDto);
}
