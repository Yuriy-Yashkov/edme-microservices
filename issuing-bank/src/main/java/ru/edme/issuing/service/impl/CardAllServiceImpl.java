package ru.edme.issuing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.issuing.dto.CardDto;
import ru.edme.issuing.exception.EntityNotFoundException;
import ru.edme.issuing.mapper.AccountMapper;
import ru.edme.issuing.mapper.CardMapper;
import ru.edme.issuing.mapper.CardStatusMapper;
import ru.edme.issuing.mapper.PaymentSystemMapper;
import ru.edme.issuing.model.Account;
import ru.edme.issuing.model.Card;
import ru.edme.issuing.repository.CardRepository;
import ru.edme.issuing.service.CardService;
import ru.edme.issuing.service.kafka.CardProducerService;
import ru.edme.issuing.util.ListWrapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardAllServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardStatusMapper cardStatusMapper;
    private final PaymentSystemMapper paymentSystemMapper;
    private final AccountMapper accountMapper;
    private final CardMapper cardMapper;
    private final CardProducerService cardProducerService;
    private final Class<Card> entityClass = Card.class;

    @Override
    @Transactional
    @CachePut(value = "card", key = "#result.id")
    public CardDto save(CardDto entity) {
        LocalDateTime dateTime = LocalDateTime.now();
        entity.setSentToProcessingCenter(dateTime);

        Card saved = cardRepository.save(cardMapper.toCard(entity));
        cardProducerService.sendCard(entity, dateTime);

        return cardMapper.toCardDto(saved);
    }

    @Override
    @Cacheable(value = "card", key = "#id")
    public CardDto findById(Long id) {
        return cardRepository.findById(id)
                .map(cardMapper::toCardDto)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                String.format("Не удалось прочитать объект! - %s = %d", entityClass.getSimpleName(), id)
                        ));
    }

    @Override
    public List<CardDto> findAll() {
        return cardRepository.findAll().stream()
                .map(cardMapper::toCardDto)
                .toList();
    }

    @Override
    @Cacheable(value = "cards", key = "'all'")
    public ListWrapper<CardDto> findAllWrapped() {
        List<CardDto> list = findAll();

        return new ListWrapper<>(list);
    }

    @Override
    @Transactional
    @CachePut(value = "card", key = "#result.id")
    public CardDto update(CardDto entity) {
        Card card = cardRepository.findById(entity.getId())
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                String.format("Не удалось прочитать объект! - %s = %d", entityClass.getSimpleName(), entity.getId())
                        ));

        card.setCardNumber(entity.getCardNumber());
        card.setExpirationDate(entity.getExpirationDate());
        card.setHolderName(entity.getHolderName());
        card.setCardStatus(cardStatusMapper.toCardStatus(entity.getCardStatus()));
        card.setPaymentSystem(paymentSystemMapper.toPaymentSystem(entity.getPaymentSystem()));

        Account account = accountMapper.toAccount(entity.getAccount());

        card.setAccount(account);
        card.setClient(account.getClient());
        card.setSentToProcessingCenter(entity.getSentToProcessingCenter());
        card.setReceivedFromProcessingCenter(entity.getReceivedFromProcessingCenter());

        Card saved = cardRepository.save(card);

        return cardMapper.toCardDto(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = "card", key = "#id")
    public boolean delete(Long id) {
        if (!cardRepository.existsById(id)) {
            return false;
        }
        cardRepository.deleteById(id);

        return true;
    }
}
