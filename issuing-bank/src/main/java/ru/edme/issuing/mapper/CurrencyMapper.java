package ru.edme.issuing.mapper;

import org.mapstruct.Mapper;
import ru.edme.issuing.dto.CurrencyDto;
import ru.edme.issuing.model.Currency;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    CurrencyDto toCurrencyDto(Currency currency);

    Currency toCurrency(CurrencyDto currencyDto);
}
