package ru.edme.processing.service;

import ru.edme.dto.CardTransferDto;

public interface CardTransferService {

    void createFromTransfer(CardTransferDto cardTransferDto);
}
