package ru.edme.processing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.processing.dto.AccountDto;
import ru.edme.processing.exception.EntityNotFoundException;
import ru.edme.processing.mapper.AccountMapper;
import ru.edme.processing.mapper.CurrencyMapper;
import ru.edme.processing.mapper.IssuingBankMapper;
import ru.edme.processing.model.Account;
import ru.edme.processing.model.Currency;
import ru.edme.processing.model.IssuingBank;
import ru.edme.processing.repository.AccountRepository;
import ru.edme.processing.repository.CurrencyRepository;
import ru.edme.processing.repository.IssuingBankRepository;
import ru.edme.processing.service.AccountAllService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountSpringAllServiceImpl implements AccountAllService {

    private final AccountRepository accountRepository;
    private final CurrencyRepository currencyRepository;
    private final IssuingBankRepository issuingBankRepository;
    private final AccountMapper accountMapper;
    private final CurrencyMapper currencyMapper;
    private final IssuingBankMapper issuingBankMapper;
    private final Class<Account> entityClass = Account.class;

    @Override
    @Transactional
    public AccountDto save(AccountDto entity) {
        Currency currency = currencyRepository.save(currencyMapper.toCurrency(entity.getCurrency()));
        IssuingBank issuingBank = issuingBankRepository.save(issuingBankMapper.toIssuingBank(entity.getIssuingBank()));

        entity.setCurrency(currencyMapper.toCurrencyDto(currency));
        entity.setIssuingBank(issuingBankMapper.toIssuingBankDto(issuingBank));

        Account account = accountMapper.toAccount(entity);
        Account saved = accountRepository.save(account);

        return accountMapper.toAccountDto(saved);
    }

    @Override
    public AccountDto findById(Long id) {
        return accountRepository.findById(id)
                .map(accountMapper::toAccountDto)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                String.format("Не удалось прочитать объект! - %s = %d", entityClass.getSimpleName(), id))
                );
    }

    @Override
    public List<AccountDto> findAll() {
        return accountRepository.findAll().stream()
                .map(accountMapper::toAccountDto)
                .toList();
    }

    @Override
    @Transactional
    public AccountDto update(AccountDto entity) {
        AccountDto accountDto = findById(entity.getId());
        accountDto.setAccountNumber(entity.getAccountNumber());
        accountDto.setBalance(entity.getBalance());
        accountDto.setCurrency(entity.getCurrency());
        accountDto.setIssuingBank(entity.getIssuingBank());
        Account account = accountMapper.toAccount(accountDto);
        Account saved = accountRepository.save(account);

        return accountMapper.toAccountDto(saved);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        AccountDto accountDto = findById(id);
        Account account = accountMapper.toAccount(accountDto);
        accountRepository.delete(account);

        return true;
    }
}
