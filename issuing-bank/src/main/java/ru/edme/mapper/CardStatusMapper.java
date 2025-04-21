package ru.edme.mapper;

import org.mapstruct.Mapper;
import ru.edme.dto.CardStatusDto;
import ru.edme.model.CardStatus;

@Mapper(componentModel = "spring")
public interface CardStatusMapper {

    CardStatusDto toCardStatusDto(CardStatus cardStatus);

    CardStatus toCardStatus(CardStatusDto cardStatusDto);
}
