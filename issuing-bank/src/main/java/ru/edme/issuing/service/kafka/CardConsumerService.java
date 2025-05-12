package ru.edme.issuing.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.dto.CardTransferDto;
import ru.edme.issuing.dto.AccountDto;
import ru.edme.issuing.dto.CardDto;
import ru.edme.issuing.dto.CardStatusDto;
import ru.edme.issuing.dto.ClientDto;
import ru.edme.issuing.dto.PaymentSystemDto;
import ru.edme.issuing.mapper.CardMapper;
import ru.edme.issuing.model.Card;
import ru.edme.issuing.repository.CardRepository;
import ru.edme.issuing.service.AccountService;
import ru.edme.issuing.service.AllService;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardConsumerService {

    private final CardRepository cardRepository;
    private final AllService<CardStatusDto, Long> cardStatusAllServiceImpl;
    private final AllService<PaymentSystemDto, Long> paymentSystemAllServiceImpl;
    private final AccountService accountService;
    private final AllService<ClientDto, Long> clientAllServiceImpl;
    private final CardMapper cardMapper;

    @Transactional
    @KafkaListener(topics = "#{@environment.getProperty('spring.kafka.topics.card-transfer-to-issuing')}",
            groupId = "card-processing-consumer-group")
    public void listen(CardTransferDto dto) {
        log.info("Получена карточка из Kafka: {}", dto);

        CardStatusDto cardStatusDto = cardStatusAllServiceImpl.findById(dto.getCardStatusId());
        PaymentSystemDto paymentSystemDto = paymentSystemAllServiceImpl.findById(dto.getPaymentSystemId());
        AccountDto accountDto = accountService.findById(dto.getAccountId());
        ClientDto clientDto = clientAllServiceImpl.findById(1L);

        CardDto card = new CardDto(
                0,
                dto.getCardNumber(),
                dto.getExpirationDate(),
                dto.getHolderName(),
                cardStatusDto,
                paymentSystemDto,
                accountDto,
                clientDto,
                null,
                LocalDateTime.now()
        );
        Card saved = cardRepository.save(cardMapper.toCard(card));
        log.info("Карточка из Kafka сохранена в БД: {}", saved);
    }
}
