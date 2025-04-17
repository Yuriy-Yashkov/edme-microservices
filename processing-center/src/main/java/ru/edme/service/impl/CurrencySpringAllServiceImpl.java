package ru.edme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.dto.CurrencyDto;
import ru.edme.mapper.CurrencyMapper;
import ru.edme.model.Currency;
import ru.edme.repository.CurrencyRepository;
import ru.edme.service.CurrencyAllService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CurrencySpringAllServiceImpl implements CurrencyAllService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;
    private final Class<Currency> entityClass = Currency.class;

    @Override
    @Transactional
    public CurrencyDto save(CurrencyDto entity) {
        Currency currency = currencyMapper.toCurrency(entity);
        Currency saved = currencyRepository.save(currency);

        return currencyMapper.toCurrencyDto(saved);
    }

    @Override
    public CurrencyDto findById(Long id) {
        return currencyRepository.findById(id)
                .map(currencyMapper::toCurrencyDto)
                .orElseThrow(
                        () -> new RuntimeException(
                                String.format("Не удалось прочитать объект! - %s = %d", entityClass.getSimpleName(), id))
                );
    }

    @Override
    public List<CurrencyDto> findAll() {
        return currencyRepository.findAll().stream()
                .map(currencyMapper::toCurrencyDto)
                .toList();
    }

    @Override
    @Transactional
    public CurrencyDto update(CurrencyDto entity) {
        CurrencyDto currencyDto = findById(entity.getId());
        currencyDto.setCurrencyDigitalCode(entity.getCurrencyDigitalCode());
        currencyDto.setCurrencyLetterCode(entity.getCurrencyLetterCode());
        currencyDto.setCurrencyName(entity.getCurrencyName());
        Currency currency = currencyMapper.toCurrency(currencyDto);
        Currency saved = currencyRepository.save(currency);

        return currencyMapper.toCurrencyDto(saved);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        CurrencyDto currencyDto = findById(id);
        Currency currency = currencyMapper.toCurrency(currencyDto);
        currencyRepository.delete(currency);

        return true;
    }
}
