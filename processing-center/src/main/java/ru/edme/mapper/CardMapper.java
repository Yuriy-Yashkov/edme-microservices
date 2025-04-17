package ru.edme.mapper;

import org.mapstruct.Mapper;
import ru.edme.dto.CardDto;
import ru.edme.model.Card;

@Mapper(componentModel = "spring")
public interface CardMapper {

    CardDto toCardDto(Card card);

    Card toCard(CardDto cardDto);
}
