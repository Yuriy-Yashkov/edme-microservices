package ru.edme.sales.mapper;

import org.mapstruct.Mapper;
import ru.edme.dto.CardTransferDto;
import ru.edme.sales.dto.requestDto.CardRequestDto;
import ru.edme.sales.dto.responseDto.CardResponseDto;
import ru.edme.sales.model.Card;

@Mapper(componentModel = "spring")
public interface CardMapper {

    Card toCard(CardRequestDto cardRequestDTO);

    Card toCard(CardResponseDto cardResponseDTO);

    CardResponseDto toCardResponseDto(Card card);

    CardRequestDto toCardRequestDto(CardResponseDto cardResponseDTO);

    CardRequestDto toCardRequestDto(CardTransferDto cardTransferDto);
}
