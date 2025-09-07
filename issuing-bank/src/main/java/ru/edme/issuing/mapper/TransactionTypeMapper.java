package ru.edme.issuing.mapper;

import org.mapstruct.Mapper;
import ru.edme.issuing.dto.TransactionTypeDto;
import ru.edme.issuing.model.TransactionType;

@Mapper(componentModel = "spring")
public interface TransactionTypeMapper {

    TransactionTypeDto toTransactionTypeDto(TransactionType transactionType);

    TransactionType toTransactionType(TransactionTypeDto transactionTypeDto);
}
