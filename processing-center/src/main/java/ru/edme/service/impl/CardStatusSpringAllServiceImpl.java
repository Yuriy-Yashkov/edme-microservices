package ru.edme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.dto.CardStatusDto;
import ru.edme.mapper.CardStatusMapper;
import ru.edme.model.CardStatus;
import ru.edme.repository.CardStatusRepository;
import ru.edme.service.CardStatusAllService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardStatusSpringAllServiceImpl implements CardStatusAllService {

    private final CardStatusRepository cardStatusRepository;
    private final CardStatusMapper cardStatusMapper;
    private final Class<CardStatus> entityClass = CardStatus.class;

    @Override
    @Transactional
    public CardStatusDto save(CardStatusDto entity) {
        CardStatus cardStatus = cardStatusMapper.toCardStatus(entity);
        CardStatus saved = cardStatusRepository.save(cardStatus);

        return cardStatusMapper.toCardStatusDto(saved);
    }

    @Override
    public CardStatusDto findById(Long id) {
        return cardStatusRepository.findById(id)
                .map(cardStatusMapper::toCardStatusDto)
                .orElseThrow(
                        () -> new RuntimeException(
                                String.format("Не удалось прочитать объект! - %s = %d", entityClass.getSimpleName(), id))
                );
    }

    @Override
    public List<CardStatusDto> findAll() {
        return cardStatusRepository.findAll().stream()
                .map(cardStatusMapper::toCardStatusDto)
                .toList();
    }

    @Override
    @Transactional
    public CardStatusDto update(CardStatusDto entity) {

        CardStatusDto cardStatusDto = findById(entity.getId());
        cardStatusDto.setCardStatusName(entity.getCardStatusName());
        CardStatus cardStatus = cardStatusMapper.toCardStatus(cardStatusDto);
        CardStatus saved = cardStatusRepository.save(cardStatus);

        return cardStatusMapper.toCardStatusDto(saved);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        CardStatusDto cardStatusDto = findById(id);
        CardStatus cardStatus = cardStatusMapper.toCardStatus(cardStatusDto);
        cardStatusRepository.delete(cardStatus);

        return true;
    }
}
