package ru.edme.sales.service;

import ru.edme.sales.dto.requestDto.CardRequestDto;
import ru.edme.sales.dto.responseDto.CardResponseDto;
import ru.edme.sales.util.ListWrapper;

public interface CardService {

    CardResponseDto save(CardRequestDto entity);

    CardResponseDto findById(Long id);

    ListWrapper<CardResponseDto> findAllWrapped();

    CardResponseDto update(CardRequestDto entity);

    boolean delete(Long id);
}
