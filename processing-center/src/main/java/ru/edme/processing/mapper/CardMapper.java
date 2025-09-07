package ru.edme.processing.mapper;

import org.mapstruct.Mapper;
import ru.edme.processing.dto.CardDto;
import ru.edme.processing.model.Card;

@Mapper(componentModel = "spring")
public interface CardMapper {

    CardDto toCardDto(Card card);

    Card toCard(CardDto cardDto);
}
