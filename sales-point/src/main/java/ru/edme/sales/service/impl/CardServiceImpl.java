package ru.edme.sales.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.dto.CardTransferDto;
import ru.edme.sales.dto.requestDto.CardRequestDto;
import ru.edme.sales.dto.responseDto.CardResponseDto;
import ru.edme.sales.dto.responseDto.PaymentSystemResponseDto;
import ru.edme.sales.exception.EntityNotFoundException;
import ru.edme.sales.feignClient.ProcessingCenterClient;
import ru.edme.sales.mapper.CardMapper;
import ru.edme.sales.mapper.PaymentSystemMapper;
import ru.edme.sales.model.Card;
import ru.edme.sales.model.PaymentSystem;
import ru.edme.sales.repository.CardRepository;
import ru.edme.sales.repository.PaymentSystemRepository;
import ru.edme.sales.service.CardService;
import ru.edme.sales.service.CardTransferService;
import ru.edme.sales.service.PaymentSystemService;
import ru.edme.sales.util.ListWrapper;
import ru.edme.sales.util.MoonAlgorithm;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardServiceImpl implements CardService, CardTransferService {

    private final CardRepository cardRepository;
    private final PaymentSystemRepository paymentSystemRepository;
    private final PaymentSystemService paymentSystemService;
    private final CardMapper cardMapper;
    private final PaymentSystemMapper paymentSystemMapper;
    private final ProcessingCenterClient processingCenterClient;

    /**
     * Принимает и преобразовывает CardTransferDto, с записью в БД.
     *
     * @param cardTransferDto
     */
    @Override
    @Transactional
    public void createFromTransfer(CardTransferDto cardTransferDto) {
        log.info("Карта из processing-center принята.");

        PaymentSystemResponseDto paymentSystemResponseDto = paymentSystemService
                .findById(cardTransferDto.getPaymentSystemId());
        CardRequestDto requestDto = cardMapper.toCardRequestDto(cardTransferDto);
        requestDto.setPaymentSystem(paymentSystemMapper.toPaymentSystemRequestDto(paymentSystemResponseDto));
        CardResponseDto saved = save(requestDto);

        log.info("Карта из processing-center записана в БД. - {}", saved);
    }

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

        // TODO: 13.05.2025    processingCenterClient.transferToProcessingCenter();

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

    // TODO: 13.05.2025 Из sales-point в processing-center нечего отправлять! Нужно удалить, то что написал!
    private CardTransferDto toCardTransferDtoToProcessingCenter(Card card) {
        return new CardTransferDto(
                card.getCardNumber(),
                card.getExpirationDate(),
                card.getHolderName(),
                null,
                card.getPaymentSystem().getId(),
                null,
                null,
                null
        );
    }
}
