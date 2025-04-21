package ru.edme.mapper;

import org.mapstruct.Mapper;
import ru.edme.dto.TransactionTypeDto;
import ru.edme.model.TransactionType;

@Mapper(componentModel = "spring")
public interface TransactionTypeMapper {

    TransactionTypeDto toTransactionTypeDto(TransactionType transactionType);

    TransactionType toTransactionType(TransactionTypeDto transactionTypeDto);
}
