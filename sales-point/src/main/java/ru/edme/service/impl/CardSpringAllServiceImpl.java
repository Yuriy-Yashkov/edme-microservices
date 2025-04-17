package ru.edme.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.dto.requestDto.CardRequestDto;
import ru.edme.dto.responseDto.CardResponseDto;
import ru.edme.exception.EntityNotFoundException;
import ru.edme.mapper.CardMapper;
import ru.edme.mapper.PaymentSystemMapper;
import ru.edme.model.Card;
import ru.edme.model.PaymentSystem;
import ru.edme.repository.CardRepository;
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
    private final PaymentSystemRepository paymentSystemRepository;
    private final CardMapper cardMapper;
    private final PaymentSystemMapper paymentSystemMapper;

    @Override
    @Transactional
    @CachePut(value = "card", key = "#result.id")
    public CardResponseDto save(CardRequestDto cardRequestDTO) {
        if (!MoonAlgorithm.isValidMoon(cardRequestDTO.getCardNumber())) {
            log.warn("Некорректный номер карты!");
            return CardResponseDto.builder().build();
        }

        // Сохраняем вложенные объекты
        PaymentSystem paymentSystem = paymentSystemMapper.toPaymentSystem(cardRequestDTO.getPaymentSystem());
        PaymentSystem paymentSystemNew = paymentSystemRepository.save(paymentSystem);

        Card card = cardMapper.toCard(cardRequestDTO);
        card.setPaymentSystem(paymentSystemNew);
        Card saved = cardRepository.save(card);

        return cardMapper.toCardResponseDto(saved);
    }

    @Override
    @Cacheable(value = "card", key = "#id")
    public CardResponseDto findById(Long id) {
        return cardRepository.findById(id)
                .map(cardMapper::toCardResponseDto)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                String.format("Не удалось прочитать объект! - %s = %d", Card.class.getSimpleName(), id))
                );
    }

    @Override
    @Cacheable(value = "cards", key = "'all'")
    public List<CardResponseDto> findAll() {
        return cardRepository.findAll().stream()
                .map(cardMapper::toCardResponseDto)
                .toList();
    }

    @Override
    @Transactional
    @CachePut(value = "card", key = "#result.id")
    public CardResponseDto update(CardRequestDto cardRequestDTO) {
        Card card = cardMapper.toCard(findById(cardRequestDTO.getId()));
        card.setCardNumber(cardRequestDTO.getCardNumber());
        card.setExpirationDate(cardRequestDTO.getExpirationDate());
        card.setHolderName(cardRequestDTO.getHolderName());
        card.setPaymentSystem(paymentSystemMapper.toPaymentSystem(cardRequestDTO.getPaymentSystem()));
        Card saved = cardRepository.save(card);

        return cardMapper.toCardResponseDto(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = "card", key = "#id")
    public boolean delete(Long id) {
        Card card = cardMapper.toCard(findById(id));
        cardRepository.delete(card);

        return true;
    }
}
