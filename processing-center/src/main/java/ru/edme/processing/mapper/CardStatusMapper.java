package ru.edme.processing.mapper;

import org.mapstruct.Mapper;
import ru.edme.processing.dto.CardStatusDto;
import ru.edme.processing.model.CardStatus;

@Mapper(componentModel = "spring")
public interface CardStatusMapper {

    CardStatusDto toCardStatusDto(CardStatus cardStatus);

    CardStatus toCardStatus(CardStatusDto cardStatusDto);
}
