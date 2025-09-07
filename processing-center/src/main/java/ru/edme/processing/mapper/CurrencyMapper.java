package ru.edme.processing.mapper;

import org.mapstruct.Mapper;
import ru.edme.processing.dto.CurrencyDto;
import ru.edme.processing.model.Currency;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    CurrencyDto toCurrencyDto(Currency currency);

    Currency toCurrency(CurrencyDto currencyDto);
}
