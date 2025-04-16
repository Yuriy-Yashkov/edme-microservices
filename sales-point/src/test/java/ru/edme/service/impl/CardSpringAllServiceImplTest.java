package ru.edme.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import ru.edme.PostgreSQLContainerInitializer;
import ru.edme.dto.requestDto.CardRequestDto;
import ru.edme.dto.responseDto.CardResponseDto;
import ru.edme.mapper.CardMapper;
import ru.edme.service.CardAllService;
import ru.edme.util.TestData;

import java.util.List;

@SpringBootTest
@WithMockUser(username = "yriy", password = "123", authorities = "ADMIN")
class CardSpringAllServiceImplTest extends PostgreSQLContainerInitializer {

    @Autowired
    private CardAllService cardAllService;

    @Autowired
    private CardMapper cardMapper;

    @Test
    void saveShouldCreateObjectTest() {
        TestData testData = new TestData();
        CardRequestDto card = testData.cardRequestDto;
        long expected = 1;

        CardResponseDto cardSaved = cardAllService.save(card);
        long actual = cardSaved.getId();

        Assertions.assertTrue(expected <= actual);
    }

    @Test
    void findByIdShouldReturnObjectTest() {
        CardResponseDto actual = cardAllService.findById(1L);

        Assertions.assertNotNull(actual);
    }

    @Test
    void findByIdShouldReturnEmptyObjectTest() {
        Assertions.assertThrows(RuntimeException.class, () -> cardAllService.findById(0L));
    }

    @Test
    void findAllShouldReturnListSize() {
        int expected = 1;
        List<CardResponseDto> cardes = cardAllService.findAll();
        int actual = cardes.size();

        Assertions.assertTrue(expected <= actual);
    }

    @Test
    void updateTest() {
        CardResponseDto actual = cardAllService.findById(1L);
        actual.setHolderName("ZZZZZZZZZZZ");
        CardRequestDto cardRequestDto = cardMapper.toCardRequestDto(actual);

        cardAllService.update(cardRequestDto);
        CardResponseDto expected = cardAllService.findById(1L);

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