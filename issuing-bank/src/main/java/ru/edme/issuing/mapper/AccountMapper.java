package ru.edme.issuing.mapper;

import org.mapstruct.Mapper;
import ru.edme.issuing.dto.AccountDto;
import ru.edme.issuing.model.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDto toAccountDto(Account account);

    Account toAccount(AccountDto accountDTO);
}
