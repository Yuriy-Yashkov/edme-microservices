package ru.edme.issuing.service;

import ru.edme.issuing.dto.CardDto;
import ru.edme.issuing.util.ListWrapper;

public interface CardService extends AllService<CardDto, Long> {

    ListWrapper<CardDto> findAllWrapped();
}
