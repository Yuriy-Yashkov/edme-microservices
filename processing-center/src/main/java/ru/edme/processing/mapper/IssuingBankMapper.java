package ru.edme.processing.mapper;

import org.mapstruct.Mapper;
import ru.edme.processing.dto.IssuingBankDto;
import ru.edme.processing.model.IssuingBank;

@Mapper(componentModel = "spring")
public interface IssuingBankMapper {

    IssuingBankDto toIssuingBankDto(IssuingBank issuingBank);

    IssuingBank toIssuingBank(IssuingBankDto issuingBankDto);
}
