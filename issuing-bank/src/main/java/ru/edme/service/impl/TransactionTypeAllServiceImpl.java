package ru.edme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edme.dto.TransactionTypeDto;
import ru.edme.exception.EntityNotFoundException;
import ru.edme.mapper.TransactionTypeMapper;
import ru.edme.model.TransactionType;
import ru.edme.repository.TransactionTypeRepository;
import ru.edme.service.AllService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionTypeAllServiceImpl implements AllService<TransactionTypeDto, Long> {

    private final TransactionTypeRepository transactionTypeRepository;
    private final TransactionTypeMapper transactionTypeMapper;
    private final Class<TransactionType> entityClass = TransactionType.class;

    @Override
    public TransactionTypeDto save(TransactionTypeDto entity) {
        TransactionType saved = transactionTypeRepository.save(transactionTypeMapper.toTransactionType(entity));

        return transactionTypeMapper.toTransactionTypeDto(saved);
    }

    @Override
    public TransactionTypeDto findById(Long id) {
        return transactionTypeRepository.findById(id)
                .map(transactionTypeMapper::toTransactionTypeDto)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                String.format("Не удалось прочитать объект! - %s = %d", entityClass.getSimpleName(), id)
                        ));
    }

    @Override
    public List<TransactionTypeDto> findAll() {
        return transactionTypeRepository.findAll().stream()
                .map(transactionTypeMapper::toTransactionTypeDto)
                .toList();
    }

    @Override
    public TransactionTypeDto update(TransactionTypeDto entity) {
        TransactionTypeDto transactionTypeDto = findById(entity.getId());
        transactionTypeDto.setTransactionTypeName(
                entity.getTransactionTypeName() != null
                        ? entity.getTransactionTypeName()
                        : transactionTypeDto.getTransactionTypeName());

        return save(transactionTypeDto);
    }

    @Override
    public boolean delete(Long id) {
        TransactionTypeDto transactionTypeDto = findById(id);
        transactionTypeRepository.delete(transactionTypeMapper.toTransactionType(transactionTypeDto));

        return true;
    }
}
