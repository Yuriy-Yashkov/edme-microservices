package ru.edme.sales.service;

import ru.edme.dto.CardTransferDto;

public interface CardTransferService {

    void createFromTransfer(CardTransferDto cardTransferDto);
}
