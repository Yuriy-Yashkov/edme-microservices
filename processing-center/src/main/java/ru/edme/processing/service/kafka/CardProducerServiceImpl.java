package ru.edme.processing.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.edme.dto.CardTransferDto;
import ru.edme.processing.dto.CardDto;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardProducerServiceImpl implements CardProducerService {

    private final KafkaTemplate<String, CardTransferDto> kafkaTemplate;

    @Value("${spring.kafka.topics.key}")
    private String key;

    @Value("${spring.kafka.topics.card-transfer-to-issuing}")
    private String topic;

    @Override
    public void sendCard(CardDto cardDto, LocalDateTime dateTime) {
        CardTransferDto cardTransferDto = mapperToCardTransferDto(cardDto, dateTime);
        CompletableFuture<SendResult<String, CardTransferDto>> future = kafkaTemplate.send(topic, key, cardTransferDto);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Ошибка при отправке сообщения в Kafka", ex);
            } else {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("Отправлено в Kafka: topic={}, partition={}, offset={}, timestamp={}",
                        metadata.topic(), metadata.partition(), metadata.offset(), metadata.timestamp());
            }
        });
    }

    private CardTransferDto mapperToCardTransferDto(CardDto cardDto, LocalDateTime dateTime) {
        return new CardTransferDto(
                cardDto.getCardNumber(),
                cardDto.getExpirationDate(),
                cardDto.getHolderName(),
                cardDto.getCardStatus().getId(),
                cardDto.getPaymentSystem().getId(),
                cardDto.getAccount().getId(),
                null,
                dateTime
        );
    }
}
