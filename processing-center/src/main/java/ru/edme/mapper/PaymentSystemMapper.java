package ru.edme.mapper;

import org.mapstruct.Mapper;
import ru.edme.dto.PaymentSystemDto;
import ru.edme.model.PaymentSystem;

@Mapper(componentModel = "spring")
public interface PaymentSystemMapper {

    PaymentSystemDto toPaymentSystemDto(PaymentSystem paymentSystem);

    PaymentSystem toPaymentSystem(PaymentSystemDto paymentSystemDto);
}
