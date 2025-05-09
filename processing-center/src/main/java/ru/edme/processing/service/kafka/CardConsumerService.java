package ru.edme.processing.service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.edme.processing.dto.CardTransferDto;

@Service
public class CardConsumerService {

    @KafkaListener(topics = "card-transfer-topic", groupId = "card-consumer-group")
    public void listen(CardTransferDto dto) {
        System.out.println("Получена карточка из Kafka: " + dto);

        // Здесь сохраняем карточку в БД processing-center (через сервис)
    }
}
