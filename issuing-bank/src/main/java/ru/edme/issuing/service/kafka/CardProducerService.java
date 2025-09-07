package ru.edme.issuing.service.kafka;

import ru.edme.issuing.dto.CardDto;

import java.time.LocalDateTime;

public interface CardProducerService {

    void sendCard(CardDto dto, LocalDateTime dateTime);
}
