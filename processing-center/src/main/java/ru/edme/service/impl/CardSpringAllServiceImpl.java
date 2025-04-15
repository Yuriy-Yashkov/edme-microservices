package ru.edme.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Override
    @Transactional
    public Card save(Card entity) {
        if (!MoonAlgorithm.isValidMoon(entity.getCardNumber())) {
            log.warn("Некорректный номер карты!");
            return Card.builder().build();
        }
        // TODO: 21.03.2025 Сохраняем вложенные объекты
        CardStatus cardStatus = cardStatusRepository.save(entity.getCardStatus());
        PaymentSystem paymentSystem = paymentSystemRepository.save(entity.getPaymentSystem());

        // TODO: 21.03.2025 Сохраняем вложенные объекты в другие вложенные объекты
        Currency currency = currencyRepository.save(entity.getAccount().getCurrency());
        IssuingBank issuingBank = issuingBankRepository.save(entity.getAccount().getIssuingBank());
        Account account = entity.getAccount();
        account.setIssuingBank(issuingBank);
        account.setCurrency(currency);
        Account accountNew = accountRepository.save(account);

        entity.setAccount(accountNew);
        entity.setCardStatus(cardStatus);
        entity.setPaymentSystem(paymentSystem);

        return cardRepository.save(entity);
    }

    @Override
    public Card findById(Long id) {
        return cardRepository.findById(id).orElseThrow(
                () -> new RuntimeException(
                        String.format("Не удалось прочитать объект! - %s = %d", Card.class.getSimpleName(), id))
        );
    }

    @Override
    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    @Override
    @Transactional
    public Card update(Card entity) {
        Card card = findById(entity.getId());
        card.setCardNumber(entity.getCardNumber());
        card.setExpirationDate(entity.getExpirationDate());
        card.setHolderName(entity.getHolderName());
        card.setCardStatus(entity.getCardStatus());
        card.setPaymentSystem(entity.getPaymentSystem());
        card.setAccount(entity.getAccount());
        card.setReceivedFromIssuingBank(entity.getReceivedFromIssuingBank());
        card.setSentToIssuingBank(entity.getSentToIssuingBank());

        return cardRepository.save(card);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        Card card = findById(id);
        cardRepository.delete(card);

        return true;
    }
}
