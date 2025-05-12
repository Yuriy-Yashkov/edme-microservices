package ru.edme.processing.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.processing.dto.AccountDto;
import ru.edme.processing.dto.CardDto;
import ru.edme.processing.mapper.AccountMapper;
import ru.edme.processing.mapper.CardMapper;
import ru.edme.processing.mapper.CardStatusMapper;
import ru.edme.processing.mapper.CurrencyMapper;
import ru.edme.processing.mapper.IssuingBankMapper;
import ru.edme.processing.mapper.PaymentSystemMapper;
import ru.edme.processing.model.Account;
import ru.edme.processing.model.Card;
import ru.edme.processing.model.CardStatus;
import ru.edme.processing.model.Currency;
import ru.edme.processing.model.IssuingBank;
import ru.edme.processing.model.PaymentSystem;
import ru.edme.processing.repository.AccountRepository;
import ru.edme.processing.repository.CardRepository;
import ru.edme.processing.repository.CardStatusRepository;
import ru.edme.processing.repository.CurrencyRepository;
import ru.edme.processing.repository.IssuingBankRepository;
import ru.edme.processing.repository.PaymentSystemRepository;
import ru.edme.processing.service.CardAllService;
import ru.edme.processing.service.kafka.CardProducerService;
import ru.edme.processing.util.MoonAlgorithm;

import java.time.LocalDateTime;
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
    private final CardProducerService cardProducerService;

    @Override
    @Transactional
    public CardDto save(CardDto cardDto) {
        LocalDateTime dateTime = LocalDateTime.now();
        cardDto.setSentToIssuingBank(dateTime);

        if (!MoonAlgorithm.isValidMoon(cardDto.getCardNumber())) {
            log.warn("Некорректный номер карты!");
            return CardDto.builder().build();
        }
        // Сохраняем вложенные объекты
        CardStatus cardStatus = cardStatusRepository.save(cardStatusMapper.toCardStatus(cardDto.getCardStatus()));
        PaymentSystem paymentSystem = paymentSystemRepository.save(paymentSystemMapper.toPaymentSystem(cardDto.getPaymentSystem()));

        // Сохраняем вложенные объекты в другие вложенные объекты
        Currency currency = currencyRepository.save(currencyMapper.toCurrency(cardDto.getAccount().getCurrency()));
        IssuingBank issuingBank = issuingBankRepository.save(issuingBankMapper.toIssuingBank(cardDto.getAccount().getIssuingBank()));

        AccountDto accountDto = cardDto.getAccount();
        accountDto.setIssuingBank(issuingBankMapper.toIssuingBankDto(issuingBank));
        accountDto.setCurrency(currencyMapper.toCurrencyDto(currency));
        Account accountNew = accountRepository.save(accountMapper.toAccount(accountDto));

        // Ложем во входящую dto реальные объекты, возвращённые из БД.
        cardDto.setAccount(accountMapper.toAccountDto(accountNew));
        cardDto.setCardStatus(cardStatusMapper.toCardStatusDto(cardStatus));
        cardDto.setPaymentSystem(paymentSystemMapper.toPaymentSystemDto(paymentSystem));

        Card card = cardMapper.toCard(cardDto);
        Card saved = cardRepository.save(card);

        cardProducerService.sendCard(cardDto, dateTime);

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
