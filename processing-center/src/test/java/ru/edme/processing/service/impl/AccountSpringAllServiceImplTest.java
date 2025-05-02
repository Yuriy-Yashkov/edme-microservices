package ru.edme.processing.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.edme.processing.PostgreSQLContainerInitializer;
import ru.edme.processing.dto.AccountDto;
import ru.edme.processing.service.AccountAllService;
import ru.edme.processing.util.TestData;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest // Запускает контекст всего приложения
//@RequiredArgsConstructor // Не хочет внедрять
//@AllArgsConstructor      // через конструктор
//@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL) // Включает автоматическое внедрение в конструктор теста.
class AccountSpringAllServiceImplTest extends PostgreSQLContainerInitializer {

    @Autowired
    private AccountAllService accountAllService;

    @Test
    void saveShouldCreateObjectTest() {
        TestData testData = new TestData();
        AccountDto accountDto = testData.accountDto;
        long expected = 1;

        AccountDto accountSaved = accountAllService.save(accountDto);
        long actual = accountSaved.getId();

        Assertions.assertTrue(expected <= actual);
    }

    @Test
    void findByIdShouldReturnObjectTest() {
        AccountDto actual = accountAllService.findById(1L);

        Assertions.assertNotNull(actual);
    }

    @Test
    void findByIdShouldReturnEmptyObjectTest() {
        Assertions.assertThrows(RuntimeException.class, () -> accountAllService.findById(0L));
    }

    @Test
    void findAllShouldReturnListSize() {
        int expected = 1;
        List<AccountDto> accountes = accountAllService.findAll();
        int actual = accountes.size();

        Assertions.assertTrue(expected <= actual);
    }

    @Test
    void updateTest() {
        AccountDto actual = accountAllService.findById(1L);
        actual.setBalance(new BigDecimal("1000.0"));

        accountAllService.update(actual);
        AccountDto expected = accountAllService.findById(1L);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteShouldReturnTrueTest() {
        boolean actual = accountAllService.delete(1L);
        boolean expected = true;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteShouldReturnRuntimeExceptionTest() {
        accountAllService.delete(1L);

        Assertions.assertThrows(RuntimeException.class, () -> accountAllService.findById(1L));
    }
}