package ru.edme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edme.dto.CardStatusDto;
import ru.edme.exception.EntityNotFoundException;
import ru.edme.mapper.CardStatusMapper;
import ru.edme.model.CardStatus;
import ru.edme.repository.CardStatusRepository;
import ru.edme.service.AllService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardStatusAllServiceImpl implements AllService<CardStatusDto, Long> {

    private final CardStatusRepository cardStatusRepository;
    private final CardStatusMapper cardStatusMapper;
    private final Class<CardStatus> entityClass = CardStatus.class;

    @Override
    public CardStatusDto save(CardStatusDto entity) {
        CardStatus saved = cardStatusRepository.save(cardStatusMapper.toCardStatus(entity));

        return cardStatusMapper.toCardStatusDto(saved);
    }

    @Override
    public CardStatusDto findById(Long id) {
        return cardStatusRepository.findById(id)
                .map(cardStatusMapper::toCardStatusDto)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                String.format("Не удалось прочитать объект! - %s = %d", entityClass.getSimpleName(), id)
                        ));
    }

    @Override
    public List<CardStatusDto> findAll() {
        return cardStatusRepository.findAll().stream()
                .map(cardStatusMapper::toCardStatusDto)
                .toList();
    }

    @Override
    public CardStatusDto update(CardStatusDto entity) {
        CardStatusDto cardStatusDto = findById(entity.getId());
        cardStatusDto.setCardStatusName(entity.getCardStatusName());

        return save(cardStatusDto);
    }

    @Override
    public boolean delete(Long id) {
        CardStatusDto cardStatusDto = findById(id);
        cardStatusRepository.delete(cardStatusMapper.toCardStatus(cardStatusDto));

        return true;
    }
}
