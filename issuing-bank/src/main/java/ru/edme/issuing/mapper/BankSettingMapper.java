package ru.edme.issuing.mapper;

import org.mapstruct.Mapper;
import ru.edme.issuing.dto.BankSettingDto;
import ru.edme.issuing.model.BankSetting;

@Mapper(componentModel = "spring")
public interface BankSettingMapper {

    BankSettingDto toBankSettingDto(BankSetting bankSetting);

    BankSetting toBankSetting(BankSettingDto bankSettingDto);
}
