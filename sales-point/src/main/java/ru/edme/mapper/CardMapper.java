package ru.edme.mapper;

import org.mapstruct.Mapper;
import ru.edme.dto.requestDto.CardRequestDto;
import ru.edme.dto.responseDto.CardResponseDto;
import ru.edme.model.Card;

@Mapper(componentModel = "spring")
public interface CardMapper {

    Card toCard(CardRequestDto cardRequestDTO);

    Card toCard(CardResponseDto cardResponseDTO);

    CardResponseDto toCardResponseDto(Card card);

    CardRequestDto toCardRequestDto(CardResponseDto cardResponseDTO);
}
