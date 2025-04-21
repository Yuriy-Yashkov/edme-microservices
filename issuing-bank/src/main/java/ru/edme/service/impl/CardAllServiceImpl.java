package ru.edme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edme.dto.CardDto;
import ru.edme.exception.EntityNotFoundException;
import ru.edme.mapper.CardMapper;
import ru.edme.model.Card;
import ru.edme.model.Transaction;
import ru.edme.repository.CardRepository;
import ru.edme.service.AllService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardAllServiceImpl implements AllService<CardDto, Long> {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final Class<Transaction> entityClass = Transaction.class;

    @Override
    public CardDto save(CardDto entity) {
        Card saved = cardRepository.save(cardMapper.toCard(entity));

        return cardMapper.toCardDto(saved);
    }

    @Override
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
    public CardDto update(CardDto entity) {
        CardDto cardDto = findById(entity.getId());
        cardDto.setCardNumber(entity.getCardNumber());
        cardDto.setExpirationDate(entity.getExpirationDate());
        cardDto.setHolderName(entity.getHolderName());
        cardDto.setCardStatus(entity.getCardStatus());
        cardDto.setPaymentSystem(entity.getPaymentSystem());
        cardDto.setAccount(entity.getAccount());
        cardDto.setClient(entity.getClient());
        cardDto.setSentToProcessingCenter(entity.getSentToProcessingCenter());
        cardDto.setReceivedFromProcessingCenter(entity.getReceivedFromProcessingCenter());

        return save(cardDto);
    }

    @Override
    public boolean delete(Long id) {
        CardDto cardDto = findById(id);
        cardRepository.delete(cardMapper.toCard(cardDto));

        return true;
    }
}
