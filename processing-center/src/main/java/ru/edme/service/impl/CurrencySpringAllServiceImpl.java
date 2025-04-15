package ru.edme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.model.Currency;
import ru.edme.repository.CurrencyRepository;
import ru.edme.service.CurrencyAllService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CurrencySpringAllServiceImpl implements CurrencyAllService {

    private final CurrencyRepository currencyRepository;
    private final Class<Currency> entityClass = Currency.class;

    @Override
    @Transactional
    public Currency save(Currency entity) {
        return currencyRepository.save(entity);
    }

    @Override
    public Currency findById(Long id) {
        return currencyRepository.findById(id).orElseThrow(
                () -> new RuntimeException(
                        String.format("Не удалось прочитать объект! - %s = %d", entityClass.getSimpleName(), id))
        );
    }

    @Override
    public List<Currency> findAll() {
        return currencyRepository.findAll();
    }

    @Override
    @Transactional
    public Currency update(Currency entity) {
        Currency currency = findById(entity.getId());
        currency.setCurrencyDigitalCode(entity.getCurrencyDigitalCode());
        currency.setCurrencyLetterCode(entity.getCurrencyLetterCode());
        currency.setCurrencyName(entity.getCurrencyName());
        return currencyRepository.save(currency);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        Currency currency = findById(id);
        currencyRepository.delete(currency);
        return true;
    }
}
