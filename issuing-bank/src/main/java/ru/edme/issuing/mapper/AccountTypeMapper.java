package ru.edme.issuing.mapper;

import org.mapstruct.Mapper;
import ru.edme.issuing.dto.AccountTypeDto;
import ru.edme.issuing.model.AccountType;

@Mapper(componentModel = "spring")
public interface AccountTypeMapper {

    AccountTypeDto toAccountTypeDto(AccountType accountType);

    AccountType toAccountType(AccountTypeDto accountTypeDto);
}
