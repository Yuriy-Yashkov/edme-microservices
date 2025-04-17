package ru.edme.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.dto.AccountDto;
import ru.edme.dto.CardDto;
import ru.edme.mapper.AccountMapper;
import ru.edme.mapper.CardMapper;
import ru.edme.mapper.CardStatusMapper;
import ru.edme.mapper.CurrencyMapper;
import ru.edme.mapper.IssuingBankMapper;
import ru.edme.mapper.PaymentSystemMapper;
import ru.edme.model.Account;
import ru.edme.model.Card;
import ru.edme.model.CardStatus;
import ru.edme.model.Currency;
import ru.edme.model.IssuingBank;
import ru.edme.model.PaymentSystem;
import ru.edme.repository.AccountRepository;
import ru.edme.repository.CardRepository;
import ru.edme.repository.CardStatusRepository;
import ru.edme.repository.CurrencyRepository;
import ru.edme.repository.IssuingBankRepository;
import ru.edme.repository.PaymentSystemRepository;
import ru.edme.service.CardAllService;
import ru.edme.util.MoonAlgorithm;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardSpringAllServiceImpl implements CardAllService {

    private final CardRepository cardRepository;
    private final CardStatusRepository cardStatusRepository;
    private final PaymentSystemRepository paymentSystemRepository;
    private final AccountRepository accountRepository;
    private final CurrencyRepository currencyRepository;
    private final IssuingBankRepository issuingBankRepository;
    private final CardMapper cardMapper;
    private final CardStatusMapper cardStatusMapper;
    private final PaymentSystemMapper paymentSystemMapper;
    private final AccountMapper accountMapper;
    private final CurrencyMapper currencyMapper;
    private final IssuingBankMapper issuingBankMapper;

    @Override
    @Transactional
    public CardDto save(CardDto entity) {
        if (!MoonAlgorithm.isValidMoon(entity.getCardNumber())) {
            log.warn("Некорректный номер карты!");
            return CardDto.builder().build();
        }
        // Сохраняем вложенные объекты
        CardStatus cardStatus = cardStatusRepository.save(cardStatusMapper.toCardStatus(entity.getCardStatus()));
        PaymentSystem paymentSystem = paymentSystemRepository.save(paymentSystemMapper.toPaymentSystem(entity.getPaymentSystem()));

        // Сохраняем вложенные объекты в другие вложенные объекты
        Currency currency = currencyRepository.save(currencyMapper.toCurrency(entity.getAccount().getCurrency()));
        IssuingBank issuingBank = issuingBankRepository.save(issuingBankMapper.toIssuingBank(entity.getAccount().getIssuingBank()));

        AccountDto accountDto = entity.getAccount();
        accountDto.setIssuingBank(issuingBankMapper.toIssuingBankDto(issuingBank));
        accountDto.setCurrency(currencyMapper.toCurrencyDto(currency));
        Account accountNew = accountRepository.save(accountMapper.toAccount(accountDto));

        // Ложем во входящую dto реальные объекты, возвращённые из БД.
        entity.setAccount(accountMapper.toAccountDto(accountNew));
        entity.setCardStatus(cardStatusMapper.toCardStatusDto(cardStatus));
        entity.setPaymentSystem(paymentSystemMapper.toPaymentSystemDto(paymentSystem));

        Card card = cardMapper.toCard(entity);
        Card saved = cardRepository.save(card);

        return cardMapper.toCardDto(saved);
    }

    @Override
    public CardDto findById(Long id) {
        return cardRepository.findById(id)
                .map(cardMapper::toCardDto)
                .orElseThrow(
                        () -> new RuntimeException(
                                String.format("Не удалось прочитать объект! - %s = %d", Card.class.getSimpleName(), id))
                );
    }

    @Override
    public List<CardDto> findAll() {
        return cardRepository.findAll().stream()
                .map(cardMapper::toCardDto)
                .toList();
    }

    @Override
    @Transactional
    public CardDto update(CardDto entity) {
        CardDto cardDto = findById(entity.getId());
        cardDto.setCardNumber(entity.getCardNumber());
        cardDto.setExpirationDate(entity.getExpirationDate());
        cardDto.setHolderName(entity.getHolderName());
        cardDto.setCardStatus(entity.getCardStatus());
        cardDto.setPaymentSystem(entity.getPaymentSystem());
        cardDto.setAccount(entity.getAccount());
        cardDto.setReceivedFromIssuingBank(entity.getReceivedFromIssuingBank());
        cardDto.setSentToIssuingBank(entity.getSentToIssuingBank());
        Card card = cardMapper.toCard(cardDto);
        Card saved = cardRepository.save(card);

        return cardMapper.toCardDto(saved);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        CardDto cardDto = findById(id);
        Card card = cardMapper.toCard(cardDto);
        cardRepository.delete(card);

        return true;
    }
}
