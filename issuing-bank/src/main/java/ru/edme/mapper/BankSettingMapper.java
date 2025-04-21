package ru.edme.mapper;

import org.mapstruct.Mapper;
import ru.edme.dto.BankSettingDto;
import ru.edme.model.BankSetting;

@Mapper(componentModel = "spring")
public interface BankSettingMapper {

    BankSettingDto toBankSettingDto(BankSetting bankSetting);

    BankSetting toBankSetting(BankSettingDto bankSettingDto);
}
