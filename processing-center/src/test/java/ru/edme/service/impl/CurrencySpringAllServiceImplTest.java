package ru.edme.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.edme.PostgreSQLContainerInitializer;
import ru.edme.dto.CurrencyDto;
import ru.edme.service.CurrencyAllService;
import ru.edme.util.TestData;

import java.util.List;

@SpringBootTest
class CurrencySpringAllServiceImplTest extends PostgreSQLContainerInitializer {

    @Autowired
    private CurrencyAllService currencyAllService;

    @Test
    void saveShouldCreateObjectTest() {
        TestData testData = new TestData();
        CurrencyDto currencyDto = testData.currencyDto;
        long expected = 1;

        CurrencyDto currencySaved = currencyAllService.save(currencyDto);
        long actual = currencySaved.getId();

        Assertions.assertTrue(expected <= actual);
    }

    @Test
    void findByIdShouldReturnObjectTest() {
        CurrencyDto actual = currencyAllService.findById(1L);

        Assertions.assertNotNull(actual);
    }

    @Test
    void findByIdShouldReturnEmptyObjectTest() {
        Assertions.assertThrows(RuntimeException.class, () -> currencyAllService.findById(0L));
    }

    @Test
    void findAllShouldReturnListSize() {
        int expected = 1;
        List<CurrencyDto> currencyes = currencyAllService.findAll();
        int actual = currencyes.size();

        Assertions.assertTrue(expected <= actual);
    }

    @Test
    void updateTest() {
        CurrencyDto actual = currencyAllService.findById(1L);
        actual.setCurrencyName("ZZZZZZZZZZZ");

        currencyAllService.update(actual);
        CurrencyDto expected = currencyAllService.findById(1L);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteShouldReturnTrueTest() {
        boolean actual = currencyAllService.delete(1L);
        boolean expected = true;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteShouldReturnRuntimeExceptionTest() {
        currencyAllService.delete(1L);

        Assertions.assertThrows(RuntimeException.class, () -> currencyAllService.findById(1L));
    }
}