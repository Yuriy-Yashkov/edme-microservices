package ru.edme.issuing.mapper;

import org.mapstruct.Mapper;
import ru.edme.issuing.dto.CardDto;
import ru.edme.issuing.model.Card;

@Mapper(componentModel = "spring")
public interface CardMapper {

    CardDto toCardDto(Card card);

    Card toCard(CardDto cardDto);
}
