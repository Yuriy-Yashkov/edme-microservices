package ru.edme.processing.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.dto.CardTransferDto;
import ru.edme.processing.dto.AccountDto;
import ru.edme.processing.dto.CardDto;
import ru.edme.processing.dto.CardStatusDto;
import ru.edme.processing.dto.PaymentSystemDto;
import ru.edme.processing.feignClient.SalesPointClient;
import ru.edme.processing.mapper.CardMapper;
import ru.edme.processing.model.Card;
import ru.edme.processing.repository.CardRepository;
import ru.edme.processing.service.AccountAllService;
import ru.edme.processing.service.CardStatusAllService;
import ru.edme.processing.service.PaymentSystemAllService;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardConsumerService {

    private final CardRepository cardRepository;
    private final CardStatusAllService cardStatusAllService;
    private final PaymentSystemAllService paymentSystemAllService;
    private final AccountAllService accountAllService;
    private final SalesPointClient salesPointClient;
    private final CardMapper cardMapper;

    @Transactional
    @KafkaListener(topics = "#{@environment.getProperty('spring.kafka.topics.card-transfer-to-processing')}",
            groupId = "card-issuing-consumer-group")
    public void listen(CardTransferDto cardTransferDto) {
        log.info("Получена карточка из Kafka: {}", cardTransferDto);

        CardStatusDto cardStatusDto = cardStatusAllService.findById(cardTransferDto.getCardStatusId());
        PaymentSystemDto paymentSystemDto = paymentSystemAllService.findById(cardTransferDto.getPaymentSystemId());
        AccountDto accountDto = accountAllService.findById(cardTransferDto.getAccountId());

        CardDto card = new CardDto(
                0,
                cardTransferDto.getCardNumber(),
                cardTransferDto.getExpirationDate(),
                cardTransferDto.getHolderName(),
                cardStatusDto,
                paymentSystemDto,
                accountDto,
                LocalDateTime.now(),
                null
        );
        Card saved = cardRepository.save(cardMapper.toCard(card));

        salesPointClient.transferToSalesPoint(cardTransferDto);
        log.info("Карточка из Kafka сохранена в БД: {}", saved);
        log.info("Передана в Sales-Point");
    }
}
