package ru.edme.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.edme.PostgreSQLContainerInitializer;
import ru.edme.dto.CardStatusDto;
import ru.edme.service.CardStatusAllService;
import ru.edme.util.TestData;

import java.util.List;

@SpringBootTest // Запускает контекст всего приложения
//@RequiredArgsConstructor // Не хочет внедрять
//@AllArgsConstructor      // через конструктор
//@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL) // Включает автоматическое внедрение в конструктор теста.
class CardStatusSpringAllServiceImplTest extends PostgreSQLContainerInitializer {

    @Autowired
    private CardStatusAllService cardStatusAllService;

    @Test
    void saveShouldCreateObjectTest() {
        TestData testData = new TestData();
        CardStatusDto cardStatusDto = testData.cardStatusDto;
        long expected = 1;

        CardStatusDto cardStatusSaved = cardStatusAllService.save(cardStatusDto);
        long actual = cardStatusSaved.getId();

        Assertions.assertTrue(expected <= actual);
    }

    @Test
    void findByIdShouldReturnObjectTest() {
        CardStatusDto actual = cardStatusAllService.findById(1L);

        Assertions.assertNotNull(actual);
    }

    @Test
    void findByIdShouldReturnEmptyObjectTest() {
        Assertions.assertThrows(RuntimeException.class, () -> cardStatusAllService.findById(0L));
    }

    @Test
    void findAllShouldReturnListSize() {
        int expected = 1;
        List<CardStatusDto> cardStatuses = cardStatusAllService.findAll();
        int actual = cardStatuses.size();

        Assertions.assertTrue(expected <= actual);
    }

    @Test
    void updateTest() {
        CardStatusDto actual = cardStatusAllService.findById(1L);
        actual.setCardStatusName("ZZZZZZZZZZZ");
        cardStatusAllService.update(actual);
        CardStatusDto expected = cardStatusAllService.findById(1L);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteShouldReturnTrueTest() {
        boolean actual = cardStatusAllService.delete(1L);
        boolean expected = true;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteShouldReturnRuntimeExceptionTest() {
        cardStatusAllService.delete(1L);

        Assertions.assertThrows(RuntimeException.class, () -> cardStatusAllService.findById(1L));
    }
}