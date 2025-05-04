package ru.edme.issuing.service;

import ru.edme.issuing.dto.AccountDto;
import ru.edme.issuing.util.ListWrapper;

public interface AccountService extends AllService<AccountDto, Long> {

    ListWrapper<AccountDto> findAllWrapped();
}
