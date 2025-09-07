package ru.edme.issuing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edme.issuing.dto.CurrencyDto;
import ru.edme.issuing.exception.EntityNotFoundException;
import ru.edme.issuing.mapper.CurrencyMapper;
import ru.edme.issuing.repository.CurrencyRepository;
import ru.edme.issuing.service.AllService;

import java.util.Currency;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyAllServiceImpl implements AllService<CurrencyDto, Long> {

    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;
    private final Class<Currency> entityClass = Currency.class;

    @Override
    public CurrencyDto save(CurrencyDto entity) {
        ru.edme.issuing.model.Currency saved = currencyRepository.save(currencyMapper.toCurrency(entity));

        return currencyMapper.toCurrencyDto(saved);
    }

    @Override
    public CurrencyDto findById(Long id) {
        return currencyRepository.findById(id)
                .map(currencyMapper::toCurrencyDto)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                String.format("Не удалось прочитать объект! - %s = %d", entityClass.getSimpleName(), id)
                        ));
    }

    @Override
    public List<CurrencyDto> findAll() {
        return currencyRepository.findAll().stream()
                .map(currencyMapper::toCurrencyDto)
                .toList();
    }

    @Override
    public CurrencyDto update(CurrencyDto entity) {
        CurrencyDto currencyDto = findById(entity.getId());
        currencyDto.setCurrencyDigitalCode(entity.getCurrencyDigitalCode());
        currencyDto.setCurrencyLetterCode(entity.getCurrencyLetterCode());
        currencyDto.setCurrencyDigitalCodeAccount(entity.getCurrencyDigitalCodeAccount());
        currencyDto.setCurrencyName(entity.getCurrencyName());

        return save(currencyDto);
    }

    @Override
    public boolean delete(Long id) {
        CurrencyDto currencyDto = findById(id);
        currencyRepository.delete(currencyMapper.toCurrency(currencyDto));

        return true;
    }
}
