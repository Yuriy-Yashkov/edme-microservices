package ru.edme.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.edme.PostgreSQLContainerInitializer;
import ru.edme.dto.IssuingBankDto;
import ru.edme.service.IssuingBankAllService;
import ru.edme.util.TestData;

import java.util.List;

@SpringBootTest
class IssuingBankSpringAllServiceImplTest extends PostgreSQLContainerInitializer {

    @Autowired
    private IssuingBankAllService issuingBankAllService;

    @Test
    void saveShouldCreateObjectTest() {
        TestData testData = new TestData();
        IssuingBankDto issuingBankDto = testData.issuingBankDto;
        long expected = 1;

        IssuingBankDto issuingBankSaved = issuingBankAllService.save(issuingBankDto);
        long actual = issuingBankSaved.getId();

        Assertions.assertTrue(expected <= actual);
    }

    @Test
    void findByIdShouldReturnObjectTest() {
        IssuingBankDto actual = issuingBankAllService.findById(1L);

        Assertions.assertNotNull(actual);
    }

    @Test
    void findByIdShouldReturnEmptyObjectTest() {
        Assertions.assertThrows(RuntimeException.class, () -> issuingBankAllService.findById(0L));
    }

    @Test
    void findAllShouldReturnListSize() {
        int expected = 1;
        List<IssuingBankDto> issuingBanks = issuingBankAllService.findAll();
        int actual = issuingBanks.size();

        Assertions.assertTrue(expected <= actual);
    }

    @Test
    void updateTest() {
        IssuingBankDto actual = issuingBankAllService.findById(1L);
        actual.setAbbreviatedName("ZZZZZZZZZZZ");

        issuingBankAllService.update(actual);
        IssuingBankDto expected = issuingBankAllService.findById(1L);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteShouldReturnTrueTest() {
        boolean actual = issuingBankAllService.delete(1L);
        boolean expected = true;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteShouldReturnRuntimeExceptionTest() {
        issuingBankAllService.delete(1L);

        Assertions.assertThrows(RuntimeException.class, () -> issuingBankAllService.findById(1L));
    }
}