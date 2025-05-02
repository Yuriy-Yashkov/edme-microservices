package ru.edme.issuing.mapper;

import org.mapstruct.Mapper;
import ru.edme.issuing.dto.TransactionDto;
import ru.edme.issuing.model.Transaction;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionDto toTransactionDto(Transaction transaction);

    Transaction toTransaction(TransactionDto transactionDto);
}
