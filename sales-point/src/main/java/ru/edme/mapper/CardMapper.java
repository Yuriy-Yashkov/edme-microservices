package ru.edme.mapper;

import org.mapstruct.Mapper;
import ru.edme.dto.requestDTO.CardRequestDTO;
import ru.edme.dto.responseDTO.CardResponseDTO;
import ru.edme.model.Card;

@Mapper(componentModel = "spring")
public interface CardMapper {

    Card toCard(CardRequestDTO cardRequestDTO);

    Card toCard(CardResponseDTO cardResponseDTO);

    CardResponseDTO toCardResponseDto(Card card);

    CardRequestDTO toCardRequestDto(CardResponseDTO cardResponseDTO);
}
