package ru.edme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.edme.dto.CardDto;
import ru.edme.exception.EntityNotFoundException;
import ru.edme.mapper.AccountMapper;
import ru.edme.mapper.CardMapper;
import ru.edme.mapper.CardStatusMapper;
import ru.edme.mapper.PaymentSystemMapper;
import ru.edme.model.Account;
import ru.edme.model.Card;
import ru.edme.repository.CardRepository;
import ru.edme.service.AllService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardAllServiceImpl implements AllService<CardDto, Long> {

    private final CardRepository cardRepository;
    private  final CardStatusMapper cardStatusMapper;
    private final PaymentSystemMapper paymentSystemMapper;
    private final AccountMapper accountMapper;
    private final CardMapper cardMapper;
    private final Class<Card> entityClass = Card.class;

    @Override
    @CachePut(value = "card", key = "#result.id")
    public CardDto save(CardDto entity) {
        Card saved = cardRepository.save(cardMapper.toCard(entity));

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
    @Cacheable(value = "cards", key = "'all'")
    public List<CardDto> findAll() {
        return cardRepository.findAll().stream()
                .map(cardMapper::toCardDto)
                .toList();
    }

    @Override
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
    @CacheEvict(value = "card", key = "#id")
    public boolean delete(Long id) {
        if (!cardRepository.existsById(id)) {
            return false;
        }
        cardRepository.deleteById(id);

        return true;
    }
}
