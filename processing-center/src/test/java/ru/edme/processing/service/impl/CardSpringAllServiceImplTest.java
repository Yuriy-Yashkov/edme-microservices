package ru.edme.processing.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.edme.processing.PostgreSQLContainerInitializer;
import ru.edme.processing.dto.CardDto;
import ru.edme.processing.exception.EntityNotFoundException;
import ru.edme.processing.feignClient.SalesPointClient;
import ru.edme.processing.service.CardAllService;
import ru.edme.processing.service.kafka.CardProducerService;
import ru.edme.processing.util.TestData;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@SpringBootTest
class CardSpringAllServiceImplTest extends PostgreSQLContainerInitializer {

    @Autowired
    private CardAllService cardAllService;

    @MockBean
    private CardProducerService cardProducerService;  // отключаем Kafka

    @MockBean
    private SalesPointClient salesPointClient; // отключаем Feign клиент

    @Test
    void saveShouldCreateObjectTest() {
        TestData testData = new TestData();
        CardDto cardDto = testData.cardDto;
        long expected = 1;

        // Поведение мока можно явно указать, если нужно
        doNothing().when(cardProducerService).sendCard(any(), any());
        doNothing().when(salesPointClient).transferToSalesPoint(any());

        CardDto cardSaved = cardAllService.save(cardDto);
        long actual = cardSaved.getId();

        Assertions.assertTrue(expected <= actual);

        // проверка, что мок(метод сервиса), вызывался.
        verify(cardProducerService).sendCard(any(), any());
        verify(salesPointClient).transferToSalesPoint(any());

    }

    @Test
    void findByIdShouldReturnObjectTest() {
        CardDto actual = cardAllService.findById(1L);

        Assertions.assertNotNull(actual);
    }

    @Test
    void findByIdShouldReturnEmptyObjectTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> cardAllService.findById(0L));
    }

    @Test
    void findAllShouldReturnListSize() {
        int expected = 1;
        List<CardDto> cardes = cardAllService.findAll();
        int actual = cardes.size();

        Assertions.assertTrue(expected <= actual);
    }

    @Test
    void updateTest() {
        CardDto actual = cardAllService.findById(1L);
        actual.setHolderName("ZZZZZZZZZZZ");

        cardAllService.update(actual);
        CardDto expected = cardAllService.findById(1L);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteShouldReturnTrueTest() {
        boolean actual = cardAllService.delete(1L);
        boolean expected = true;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteShouldReturnRuntimeExceptionTest() {
        cardAllService.delete(1L);

        Assertions.assertThrows(EntityNotFoundException.class, () -> cardAllService.findById(1L));
    }
}