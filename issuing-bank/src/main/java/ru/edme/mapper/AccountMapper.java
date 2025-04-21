package ru.edme.mapper;

import org.mapstruct.Mapper;
import ru.edme.dto.AccountDto;
import ru.edme.model.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDto toAccountDto(Account account);

    Account toAccount(AccountDto accountDTO);
}
