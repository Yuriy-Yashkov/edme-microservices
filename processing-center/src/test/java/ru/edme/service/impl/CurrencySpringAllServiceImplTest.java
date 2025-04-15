package ru.edme.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.edme.PostgreSQLContainerInitializer;
import ru.edme.model.Currency;
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
        Currency currency = testData.currency;
        long expected = 1;

        Currency currencySaved = currencyAllService.save(currency);
        long actual = currencySaved.getId();

        Assertions.assertTrue(expected <= actual);
    }

    @Test
    void findByIdShouldReturnObjectTest() {
        Currency actual = currencyAllService.findById(1L);

        Assertions.assertNotNull(actual);
    }

    @Test
    void findByIdShouldReturnEmptyObjectTest() {
        Assertions.assertThrows(RuntimeException.class, () -> currencyAllService.findById(0L));
    }

    @Test
    void findAllShouldReturnListSize() {
        int expected = 1;
        List<Currency> currencyes = currencyAllService.findAll();
        int actual = currencyes.size();

        Assertions.assertTrue(expected <= actual);
    }

    @Test
    void updateTest() {
        Currency actual = currencyAllService.findById(1L);
        actual.setCurrencyName("ZZZZZZZZZZZ");

        currencyAllService.update(actual);
        Currency expected = currencyAllService.findById(1L);

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