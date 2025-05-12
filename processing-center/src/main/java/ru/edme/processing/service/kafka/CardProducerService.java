package ru.edme.processing.service.kafka;

import ru.edme.processing.dto.CardDto;

import java.time.LocalDateTime;

public interface CardProducerService {

    void sendCard(CardDto dto, LocalDateTime dateTime);
}
