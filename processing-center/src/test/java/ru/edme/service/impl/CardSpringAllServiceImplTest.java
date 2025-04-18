package ru.edme.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.edme.PostgreSQLContainerInitializer;
import ru.edme.dto.CardDto;
import ru.edme.service.CardAllService;
import ru.edme.util.TestData;

import java.util.List;

@SpringBootTest
class CardSpringAllServiceImplTest extends PostgreSQLContainerInitializer {

    @Autowired
    private CardAllService cardAllService;

    @Test
    void saveShouldCreateObjectTest() {
        TestData testData = new TestData();
        CardDto cardDto = testData.cardDto;
        long expected = 1;

        CardDto cardSaved = cardAllService.save(cardDto);
        long actual = cardSaved.getId();

        Assertions.assertTrue(expected <= actual);
    }

    @Test
    void findByIdShouldReturnObjectTest() {
        CardDto actual = cardAllService.findById(1L);

        Assertions.assertNotNull(actual);
    }

    @Test
    void findByIdShouldReturnEmptyObjectTest() {
        Assertions.assertThrows(RuntimeException.class, () -> cardAllService.findById(0L));
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

        Assertions.assertThrows(RuntimeException.class, () -> cardAllService.findById(1L));
    }
}