package ru.edme.processing.service.impl;

import feign.FeignException;
import feign.Request;
import feign.Response;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.context.TestConstructor;
import ru.edme.dto.CardTransferDto;
import ru.edme.processing.dto.CardDto;
import ru.edme.processing.exception.EmptyResponseException;
import ru.edme.processing.exception.RetryableRemoteServiceException;
import ru.edme.processing.exception.ServerErrorException;
import ru.edme.processing.feignClient.SalesPointClient;
import ru.edme.processing.mapper.AccountMapper;
import ru.edme.processing.mapper.CardMapper;
import ru.edme.processing.mapper.CardStatusMapper;
import ru.edme.processing.mapper.CurrencyMapper;
import ru.edme.processing.mapper.IssuingBankMapper;
import ru.edme.processing.mapper.PaymentSystemMapper;
import ru.edme.processing.model.Account;
import ru.edme.processing.model.Card;
import ru.edme.processing.repository.AccountRepository;
import ru.edme.processing.repository.CardRepository;
import ru.edme.processing.repository.CardStatusRepository;
import ru.edme.processing.repository.CurrencyRepository;
import ru.edme.processing.repository.IssuingBankRepository;
import ru.edme.processing.repository.PaymentSystemRepository;
import ru.edme.processing.service.AccountAllService;
import ru.edme.processing.service.CardStatusAllService;
import ru.edme.processing.service.PaymentSystemAllService;
import ru.edme.processing.service.kafka.CardProducerService;
import ru.edme.processing.util.TestData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CardSpringAllServiceImplUnitTest {

    @MockBean
    private SalesPointClient salesPointClient;

    @MockBean
    private CardRepository cardRepository;

    @MockBean
    private CardStatusRepository cardStatusRepository;

    @MockBean
    private PaymentSystemRepository paymentSystemRepository;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private CurrencyRepository currencyRepository;

    @MockBean
    private IssuingBankRepository issuingBankRepository;

    @MockBean
    private CardMapper cardMapper;

    @MockBean
    private CardStatusMapper cardStatusMapper;

    @MockBean
    private PaymentSystemMapper paymentSystemMapper;

    @MockBean
    private AccountMapper accountMapper;

    @MockBean
    private CurrencyMapper currencyMapper;

    @MockBean
    private IssuingBankMapper issuingBankMapper;

    @MockBean
    private CardProducerService cardProducerService;

    @MockBean
    private CardStatusAllService cardStatusAllService;

    @MockBean
    private PaymentSystemAllService paymentSystemAllService;

    @MockBean
    private AccountAllService accountAllService;

    @MockBean
    private RetryTemplate retryTemplate;
    private final CardSpringAllServiceImpl cardService;
    private CardDto cardDto;
    private CardTransferDto cardTransferDto;
    private Card card;

    @BeforeEach
    void setUp() {
        TestData testData = new TestData();
        cardDto = testData.cardDto;
        card = testData.card;
        cardTransferDto = new CardTransferDto(
                "4123450000000019",
                LocalDate.MIN,
                "IVAN I.IVANOV",
                1L,
                1L,
                1L,
                LocalDateTime.MIN,
                LocalDateTime.MIN
        );
        mockAllDependencies();
    }

    @Test
    void shouldThrowExceptionOn4xx() {
        FeignException feignException = FeignException.errorStatus(
                "POST",
                Response.builder()
                        .status(400)
                        .reason("Bad Request")
                        .request(Request.create(
                                Request.HttpMethod.POST,
                                "http://localhost",
                                Map.of(),
                                null,
                                null,
                                null
                        ))
                        .build()
        );

        Mockito.when(retryTemplate.execute(Mockito.any()))
                .thenThrow(feignException);

        assertThrows(FeignException.BadRequest.class, () -> cardService.save(cardDto));
    }

    @Test
    void shouldThrowRetryableExceptionOn5xx() {
        // 1. Создаём FeignException.InternalServerError
        FeignException.InternalServerError feignException =
                new FeignException.InternalServerError(
                        "Ошибка 500",
                        Request.create(Request.HttpMethod.POST, "http://localhost", Map.of(), null, null, null),
                        null,
                        null
                );

        // 2. Мокаем retryTemplate, чтобы выбрасывал наш exception (через ServerErrorException → RetryableRemoteServiceException)
        Mockito.when(retryTemplate.execute(Mockito.any()))
                .thenThrow(new RetryableRemoteServiceException("wrapped", new ServerErrorException("5xx", feignException)));

        // 3. Проверяем, что выброшено исключение нужного типа
        RetryableRemoteServiceException ex = assertThrows(RetryableRemoteServiceException.class,
                () -> cardService.save(cardDto));

        // 4. Дополнительно убеждаемся, что причина — ServerErrorException, а её причина — FeignException.InternalServerError
        assertTrue(ex.getCause() instanceof ServerErrorException);
        assertTrue(ex.getCause().getCause() instanceof FeignException.InternalServerError);
    }

    @Test
    void shouldThrowEmptyResponseException() {
        // 1. Создаём FeignException с кодом -1 (симулируем "пустой ответ")
        FeignException feignException = FeignException.errorStatus(
                "POST",
                Response.builder()
                        .status(-1)
                        .reason("Empty Response")
                        .request(Request.create(
                                Request.HttpMethod.POST,
                                "http://localhost",
                                Map.of(),
                                null,
                                null,
                                null
                        ))
                        .build()
        );

        // 2. Настраиваем retryTemplate, чтобы он выбросил этот exception
        Mockito.when(retryTemplate.execute(Mockito.any()))
                .thenThrow(new RetryableRemoteServiceException("wrapped", new EmptyResponseException("empty", feignException)));

        // 3. Проверяем, что наш сервис действительно пробрасывает RetryableRemoteServiceException
        assertThrows(RetryableRemoteServiceException.class, () -> cardService.save(cardDto));
    }

    private void mockAllDependencies() {
        Mockito.when(cardStatusMapper.toCardStatus(cardDto.getCardStatus()))
                .thenReturn(card.getCardStatus());
        Mockito.when(cardStatusRepository.save(Mockito.any()))
                .thenReturn(card.getCardStatus());
        Mockito.when(cardStatusMapper.toCardStatusDto(Mockito.any()))
                .thenReturn(cardDto.getCardStatus());

        Mockito.when(paymentSystemMapper.toPaymentSystem(cardDto.getPaymentSystem()))
                .thenReturn(card.getPaymentSystem());
        Mockito.when(paymentSystemRepository.save(Mockito.any()))
                .thenReturn(card.getPaymentSystem());
        Mockito.when(paymentSystemMapper.toPaymentSystemDto(Mockito.any()))
                .thenReturn(cardDto.getPaymentSystem());

        Mockito.when(currencyMapper.toCurrency(cardDto.getAccount().getCurrency()))
                .thenReturn(card.getAccount().getCurrency());
        Mockito.when(currencyRepository.save(Mockito.any()))
                .thenReturn(card.getAccount().getCurrency());

        Mockito.when(issuingBankMapper.toIssuingBank(cardDto.getAccount().getIssuingBank()))
                .thenReturn(card.getAccount().getIssuingBank());
        Mockito.when(issuingBankRepository.save(Mockito.any()))
                .thenReturn(card.getAccount().getIssuingBank());

        Account account = card.getAccount();
        Mockito.when(accountMapper.toAccount(Mockito.any()))
                .thenReturn(account);
        Mockito.when(accountRepository.save(Mockito.any()))
                .thenReturn(account);
        Mockito.when(accountMapper.toAccountDto(Mockito.any()))
                .thenReturn(cardDto.getAccount());

        Mockito.when(cardMapper.toCard(cardDto)).thenReturn(card);
        Mockito.when(cardRepository.save(card)).thenReturn(card);
        Mockito.when(cardMapper.toCardDto(card)).thenReturn(cardDto);

        Mockito.doNothing().when(cardProducerService).sendCard(Mockito.eq(cardDto), Mockito.any(LocalDateTime.class));
    }
}
