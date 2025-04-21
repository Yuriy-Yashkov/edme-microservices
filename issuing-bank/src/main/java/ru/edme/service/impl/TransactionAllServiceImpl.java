package ru.edme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edme.dto.TransactionDto;
import ru.edme.exception.EntityNotFoundException;
import ru.edme.mapper.TransactionMapper;
import ru.edme.model.Transaction;
import ru.edme.repository.TransactionRepository;
import ru.edme.service.AllService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionAllServiceImpl implements AllService<TransactionDto, Long> {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final Class<Transaction> entityClass = Transaction.class;

    @Override
    public TransactionDto save(TransactionDto entity) {
        Transaction saved = transactionRepository.save(transactionMapper.toTransaction(entity));

        return transactionMapper.toTransactionDto(saved);
    }

    @Override
    public TransactionDto findById(Long id) {
        return transactionRepository.findById(id)
                .map(transactionMapper::toTransactionDto)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                String.format("Не удалось прочитать объект! - %s = %d", entityClass.getSimpleName(), id)
                        ));
    }

    @Override
    public List<TransactionDto> findAll() {
        return transactionRepository.findAll().stream()
                .map(transactionMapper::toTransactionDto)
                .toList();
    }

    @Override
    public TransactionDto update(TransactionDto entity) {
        TransactionDto transactionDto = findById(entity.getId());
        transactionDto.setTransactionDate(entity.getTransactionDate());
        transactionDto.setSum(entity.getSum());
        transactionDto.setTransactionName(entity.getTransactionName());
        transactionDto.setTransactionType(entity.getTransactionType());
        transactionDto.setAccount(entity.getAccount());
        transactionDto.setSentToProcessingCenter(entity.getSentToProcessingCenter());
        transactionDto.setReceivedFromProcessingCenter(entity.getReceivedFromProcessingCenter());

        return save(transactionDto);
    }

    @Override
    public boolean delete(Long id) {
        TransactionDto transactionDto = findById(id);
        transactionRepository.delete(transactionMapper.toTransaction(transactionDto));

        return true;
    }
}
