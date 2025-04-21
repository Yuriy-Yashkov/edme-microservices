package ru.edme.mapper;

import org.mapstruct.Mapper;
import ru.edme.dto.TransactionDto;
import ru.edme.model.Transaction;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionDto toTransactionDto(Transaction transaction);

    Transaction toTransaction(TransactionDto transactionDto);
}
