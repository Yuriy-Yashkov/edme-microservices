package ru.edme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.model.Account;
import ru.edme.model.Currency;
import ru.edme.model.IssuingBank;
import ru.edme.repository.AccountRepository;
import ru.edme.repository.CurrencyRepository;
import ru.edme.repository.IssuingBankRepository;
import ru.edme.service.AccountAllService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountSpringAllServiceImpl implements AccountAllService {

    private final AccountRepository accountRepository;
    private final CurrencyRepository currencyRepository;
    private final IssuingBankRepository issuingBankRepository;
    private final Class<Account> entityClass = Account.class;

    @Override
    @Transactional
    public Account save(Account entity) {
        Currency currency = currencyRepository.save(entity.getCurrency());
        IssuingBank issuingBank = issuingBankRepository.save(entity.getIssuingBank());

        entity.setCurrency(currency);
        entity.setIssuingBank(issuingBank);

        return accountRepository.save(entity);
    }

    @Override
    public Account findById(Long id) {

        return accountRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                String.format("Не удалось прочитать объект! - %s = %d", entityClass.getSimpleName(), id))
                );
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    @Transactional
    public Account update(Account entity) {
        Account account = findById(entity.getId());
        account.setAccountNumber(entity.getAccountNumber());
        account.setBalance(entity.getBalance());
        account.setCurrency(entity.getCurrency());
        account.setIssuingBank(entity.getIssuingBank());

        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        Account account = findById(id);
        accountRepository.delete(account);

        return true;
    }
}
