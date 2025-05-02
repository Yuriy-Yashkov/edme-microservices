package ru.edme.issuing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.edme.issuing.dto.AccountDto;
import ru.edme.issuing.exception.EntityNotFoundException;
import ru.edme.issuing.mapper.AccountMapper;
import ru.edme.issuing.model.Account;
import ru.edme.issuing.repository.AccountRepository;
import ru.edme.issuing.service.AllService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountAllServiceImpl implements AllService<AccountDto, Long> {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final Class<Account> entityClass = Account.class;

    @Override
    @CachePut(value = "account", key = "#result.id")
    public AccountDto save(AccountDto entity) {
        Account saved = accountRepository.save(accountMapper.toAccount(entity));

        return accountMapper.toAccountDto(saved);
    }

    @Override
    @Cacheable(value = "account", key = "#id")
    public AccountDto findById(Long id) {
        return accountRepository.findById(id)
                .map(accountMapper::toAccountDto)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                String.format("Не удалось прочитать объект! - %s = %d", entityClass.getSimpleName(), id)
                        ));
    }

    @Override
    @Cacheable(value = "accounts", key = "'all'")
    public List<AccountDto> findAll() {
        return accountRepository.findAll().stream()
                .map(accountMapper::toAccountDto)
                .toList();
    }

    @Override
    @CachePut(value = "account", key = "#result.id")
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
    @CacheEvict(value = "account", key = "#id")
    public boolean delete(Long id) {
        AccountDto accountDto = findById(id);
        accountRepository.delete(accountMapper.toAccount(accountDto));

        return true;
    }
}
