package ru.edme.issuing.mapper;

import org.mapstruct.Mapper;
import ru.edme.issuing.dto.CardStatusDto;
import ru.edme.issuing.model.CardStatus;

@Mapper(componentModel = "spring")
public interface CardStatusMapper {

    CardStatusDto toCardStatusDto(CardStatus cardStatus);

    CardStatus toCardStatus(CardStatusDto cardStatusDto);
}
