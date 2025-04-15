package ru.edme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.model.CardStatus;
import ru.edme.repository.CardStatusRepository;
import ru.edme.service.CardStatusAllService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardStatusSpringAllServiceImpl implements CardStatusAllService {

    private final CardStatusRepository cardStatusRepository;
    private final Class<CardStatus> entityClass = CardStatus.class;

    @Override
    @Transactional
    public CardStatus save(CardStatus entity) {
        return cardStatusRepository.save(entity);
    }

    @Override
    public CardStatus findById(Long id) {
        return cardStatusRepository.findById(id).orElseThrow(
                () -> new RuntimeException(
                        String.format("Не удалось прочитать объект! - %s = %d", entityClass.getSimpleName(), id))
        );
    }

    @Override
    public List<CardStatus> findAll() {
        return cardStatusRepository.findAll();
    }

    @Override
    @Transactional
    public CardStatus update(CardStatus entity) {

        CardStatus cardStatus = findById(entity.getId());
        cardStatus.setCardStatusName(entity.getCardStatusName());

        return cardStatusRepository.save(cardStatus);
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        CardStatus cardStatusOld = findById(id);
        cardStatusRepository.delete(cardStatusOld);
        return true;
    }
}
