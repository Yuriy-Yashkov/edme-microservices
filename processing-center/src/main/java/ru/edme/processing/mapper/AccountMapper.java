package ru.edme.processing.mapper;

import org.mapstruct.Mapper;
import ru.edme.processing.dto.AccountDto;
import ru.edme.processing.model.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDto toAccountDto(Account account);

    Account toAccount(AccountDto accountDTO);
}