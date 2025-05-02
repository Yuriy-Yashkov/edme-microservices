package ru.edme.sales.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.sales.dto.requestDto.CardRequestDto;
import ru.edme.sales.dto.responseDto.CardResponseDto;
import ru.edme.sales.exception.EntityNotFoundException;
import ru.edme.sales.mapper.CardMapper;
import ru.edme.sales.mapper.PaymentSystemMapper;
import ru.edme.sales.model.Card;
import ru.edme.sales.model.PaymentSystem;
import ru.edme.sales.repository.CardRepository;
import ru.edme.sales.repository.PaymentSystemRepository;
import ru.edme.sales.service.CardService;
import ru.edme.sales.util.ListWrapper;
import ru.edme.sales.util.MoonAlgorithm;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardServiceImpl implements CardService {

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
    public ListWrapper<CardResponseDto> findAllWrapped() {
        List<CardResponseDto> list = cardRepository.findAll().stream()
                .map(cardMapper::toCardResponseDto)
                .toList();

        return new ListWrapper<>(list);
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
