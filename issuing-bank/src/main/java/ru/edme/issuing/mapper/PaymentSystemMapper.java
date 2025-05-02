package ru.edme.issuing.mapper;

import org.mapstruct.Mapper;
import ru.edme.issuing.dto.PaymentSystemDto;
import ru.edme.issuing.model.PaymentSystem;

@Mapper(componentModel = "spring")
public interface PaymentSystemMapper {

    PaymentSystemDto toPaymentSystemDto(PaymentSystem paymentSystem);

    PaymentSystem toPaymentSystem(PaymentSystemDto paymentSystemDto);
}
