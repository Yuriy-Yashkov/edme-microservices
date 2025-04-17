package ru.edme.mapper;

import org.mapstruct.Mapper;
import ru.edme.dto.IssuingBankDto;
import ru.edme.model.IssuingBank;

@Mapper(componentModel = "spring")
public interface IssuingBankMapper {

    IssuingBankDto toIssuingBankDto(IssuingBank issuingBank);

    IssuingBank toIssuingBank(IssuingBankDto issuingBankDto);
}
