package ru.edme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edme.dto.AccountDto;
import ru.edme.exception.EntityNotFoundException;
import ru.edme.mapper.AccountMapper;
import ru.edme.model.Account;
import ru.edme.model.Transaction;
import ru.edme.repository.AccountRepository;
import ru.edme.service.AllService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountAllServiceImpl implements AllService<AccountDto, Long> {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final Class<Transaction> entityClass = Transaction.class;

    @Override
    public AccountDto save(AccountDto entity) {
        Account saved = accountRepository.save(accountMapper.toAccount(entity));

        return accountMapper.toAccountDto(saved);
    }

    @Override
    public AccountDto findById(Long id) {
        return accountRepository.findById(id)
                .map(accountMapper::toAccountDto)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                String.format("Не удалось прочитать объект! - %s = %d", entityClass.getSimpleName(), id)
                        ));
    }

    @Override
    public List<AccountDto> findAll() {
        return accountRepository.findAll().stream()
                .map(accountMapper::toAccountDto)
                .toList();
    }

    @Override
    public AccountDto update(AccountDto entity) {
        AccountDto accountDto = findById(entity.getId());
        accountDto.setAccountNumber(entity.getAccountNumber());
        accountDto.setBalance(entity.getBalance());
        accountDto.setCurrency(entity.getCurrency());
        accountDto.setAccountType(entity.getAccountType());
        accountDto.setClient(entity.getClient());
        accountDto.setAccountOpeningDate(entity.getAccountOpeningDate());
        accountDto.setSuspendingOperations(entity.isSuspendingOperations());
        accountDto.setAccountClosingDate(entity.getAccountClosingDate());

        return save(accountDto);
    }

    @Override
    public boolean delete(Long id) {
        AccountDto accountDto = findById(id);
        accountRepository.delete(accountMapper.toAccount(accountDto));

        return true;
    }
}
