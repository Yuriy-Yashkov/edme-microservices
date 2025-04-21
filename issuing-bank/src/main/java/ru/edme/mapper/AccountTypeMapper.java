package ru.edme.mapper;

import org.mapstruct.Mapper;
import ru.edme.dto.AccountTypeDto;
import ru.edme.model.AccountType;

@Mapper(componentModel = "spring")
public interface AccountTypeMapper {

    AccountTypeDto toAccountTypeDto(AccountType accountType);

    AccountType toAccountType(AccountTypeDto accountTypeDto);
}
