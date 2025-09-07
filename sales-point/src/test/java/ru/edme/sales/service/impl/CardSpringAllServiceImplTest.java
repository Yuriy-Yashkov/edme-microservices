package ru.edme.sales.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import ru.edme.sales.PostgreSQLContainerInitializer;
import ru.edme.sales.dto.requestDto.CardRequestDto;
import ru.edme.sales.dto.responseDto.CardResponseDto;
import ru.edme.sales.mapper.CardMapper;
import ru.edme.sales.service.CardService;
import ru.edme.sales.util.TestData;

import java.util.List;

@SpringBootTest
@WithMockUser(username = "yriy", password = "123", authorities = "ADMIN")
class CardSpringAllServiceImplTest extends PostgreSQLContainerInitializer {

    @Autowired
    private CardService cardService;

    @Autowired
    private CardMapper cardMapper;

    @Test
    void saveShouldCreateObjectTest() {
        TestData testData = new TestData();
        CardRequestDto card = testData.cardRequestDto;
        long expected = 1;

        CardResponseDto cardSaved = cardService.save(card);
        long actual = cardSaved.getId();

        Assertions.assertTrue(expected <= actual);
    }

    @Test
    void findByIdShouldReturnObjectTest() {
        CardResponseDto actual = cardService.findById(1L);

        Assertions.assertNotNull(actual);
    }

    @Test
    void findByIdShouldReturnEmptyObjectTest() {
        Assertions.assertThrows(RuntimeException.class, () -> cardService.findById(0L));
    }

    @Test
    void findAllShouldReturnListSize() {
        int expected = 1;
        List<CardResponseDto> cardes = cardService.findAllWrapped().getValues();
        int actual = cardes.size();

        Assertions.assertTrue(expected <= actual);
    }

    @Test
    void updateTest() {
        CardResponseDto actual = cardService.findById(1L);
        actual.setHolderName("ZZZZZZZZZZZ");
        CardRequestDto cardRequestDto = cardMapper.toCardRequestDto(actual);

        cardService.update(cardRequestDto);
        CardResponseDto expected = cardService.findById(1L);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteShouldReturnTrueTest() {
        boolean actual = cardService.delete(1L);
        boolean expected = true;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteShouldReturnRuntimeExceptionTest() {
        cardService.delete(1L);

        Assertions.assertThrows(RuntimeException.class, () -> cardService.findById(1L));
    }
}