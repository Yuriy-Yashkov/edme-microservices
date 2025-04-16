package ru.edme.service;

import ru.edme.dto.requestDTO.CardRequestDTO;
import ru.edme.dto.responseDTO.CardResponseDTO;
import ru.edme.model.Card;

public interface CardAllService extends AllService<Long, Card, CardRequestDTO, CardResponseDTO> {

}
