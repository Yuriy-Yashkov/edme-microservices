package ru.edme.issuing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.edme.issuing.dto.AccountDto;
import ru.edme.issuing.dto.TransactionDto;
import ru.edme.issuing.dto.TransactionTypeDto;
import ru.edme.issuing.exception.EntityNotFoundException;
import ru.edme.issuing.mapper.TransactionMapper;
import ru.edme.issuing.model.Transaction;
import ru.edme.issuing.repository.TransactionRepository;
import ru.edme.issuing.service.AllService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionAllServiceImpl implements AllService<TransactionDto, Long> {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final AllService<AccountDto, Long> accountAllServiceImpl;
    private final AllService<TransactionTypeDto, Long> transactionTypeAllServiceImpl;
    private final Class<Transaction> entityClass = Transaction.class;

    @Override
    public TransactionDto save(TransactionDto entity) {
        AccountDto accountDto = accountAllServiceImpl.findById(entity.getAccount().getId());
        TransactionTypeDto transactionTypeDto = transactionTypeAllServiceImpl.findById(entity.getTransactionType().getId());

        entity.setAccount(accountDto);
        entity.setTransactionType(transactionTypeDto);

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
