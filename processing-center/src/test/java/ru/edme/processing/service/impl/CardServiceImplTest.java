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

@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
//@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

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

//    @Test
//    void shouldThrowExceptionOn4xx() {
//        // Создаём заглушки
//        FeignException.BadRequest exception = Mockito.mock(FeignException.BadRequest.class);
//
//        // Мокаем вложенные сохранения — упрощённо
//        Mockito.when(cardStatusMapper.toCardStatus(cardDto.getCardStatus()))
//                .thenReturn(new CardStatus());
//        Mockito.when(cardStatusRepository.save(Mockito.any()))
//                .thenReturn(new CardStatus());
//
//        Mockito.when(paymentSystemMapper.toPaymentSystem(cardDto.getPaymentSystem()))
//                .thenReturn(new PaymentSystem());
//        Mockito.when(paymentSystemRepository.save(Mockito.any()))
//                .thenReturn(new PaymentSystem());
//
//        Mockito.when(currencyMapper.toCurrency(cardDto.getAccount().getCurrency()))
//                .thenReturn(new Currency());
//        Mockito.when(currencyRepository.save(Mockito.any()))
//                .thenReturn(new Currency());
//
//        Mockito.when(issuingBankMapper.toIssuingBank(cardDto.getAccount().getIssuingBank()))
//                .thenReturn(new IssuingBank());
//        Mockito.when(issuingBankRepository.save(Mockito.any()))
//                .thenReturn(new IssuingBank());
//
//        // Мокаем Account
//        Mockito.when(accountMapper.toAccount(Mockito.any()))
//                .thenReturn(new Account());
//        Mockito.when(accountRepository.save(Mockito.any()))
//                .thenReturn(new Account());
//        Mockito.when(accountMapper.toAccountDto(Mockito.any()))
//                .thenReturn(cardDto.getAccount());
//
//        // Обновляем cardDto с замоканными account/cardStatus/paymentSystem
//        Mockito.when(cardStatusMapper.toCardStatusDto(Mockito.any()))
//                .thenReturn(cardDto.getCardStatus());
//        Mockito.when(paymentSystemMapper.toPaymentSystemDto(Mockito.any()))
//                .thenReturn(cardDto.getPaymentSystem());
//
//        // Мокаем cardMapper.toCard()
//        Card dummyCard = new Card();
//        dummyCard.setCardNumber("4123450000000019");
//        dummyCard.setExpirationDate(LocalDate.now());
//        dummyCard.setHolderName("IVAN I.IVANOV");
//
//        PaymentSystem ps = new PaymentSystem();
//        ps.setId(1L);
//        dummyCard.setPaymentSystem(ps);
//
//        Mockito.when(cardMapper.toCard(cardDto)).thenReturn(dummyCard);
//        Mockito.when(cardRepository.save(dummyCard)).thenReturn(dummyCard);
//        Mockito.when(cardMapper.toCardDto(dummyCard)).thenReturn(cardDto);
//
//        Mockito.doNothing()
//                .when(cardProducerService)
//                .sendCard(Mockito.eq(cardDto), Mockito.any(LocalDateTime.class));
//
//        // Мокаем retryTemplate.execute
//        Mockito.when(retryTemplate.execute(Mockito.any())).then(invocation -> {
//            // Внутри вызывается salesPointClient.transferToSalesPoint()
//            salesPointClient.transferToSalesPoint(Mockito.any());
//            return null;
//        });
//
//        // Мокаем бросок ошибки при вызове salesPointClient
//        Mockito.doThrow(exception)
//                .when(salesPointClient).transferToSalesPoint(Mockito.any());
//
//        // Проверка, что 4xx исключение обрабатывается правильно
//        assertThrows(FeignException.BadRequest.class,
//                () -> cardService.save(cardDto));
//    }

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
    void shouldThrowExceptionOn5xx() {
        FeignException.InternalServerError exception =
                Mockito.mock(FeignException.InternalServerError.class);

        // Настраиваем retryTemplate.execute() — внутри него выбрасывается 5xx
        Mockito.when(retryTemplate.execute(Mockito.any())).then(invocation -> {
            // Вызываем feign клиент, который бросает исключение
            throw exception;
        });

        // assertThrows, что метод сохраняет карту, но выбрасывает 5xx
        assertThrows(FeignException.InternalServerError.class, () -> cardService.save(cardDto));
    }

    @Test
    void shouldThrowExceptionOnEmptyResponse() {
        // Мокаем retryTemplate.execute() так, чтобы feign-клиент вернул null
        Mockito.when(retryTemplate.execute(Mockito.any())).then(invocation -> {
            return null; // как будто client ничего не вернул
        });

        // Ожидаем, что наш сервис бросит IllegalStateException (или свой кастомный, если есть)
        assertThrows(IllegalStateException.class, () -> cardService.save(cardDto));
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
