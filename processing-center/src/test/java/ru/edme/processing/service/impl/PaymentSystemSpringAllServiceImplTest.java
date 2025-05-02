package ru.edme.processing.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.edme.processing.PostgreSQLContainerInitializer;
import ru.edme.processing.dto.PaymentSystemDto;
import ru.edme.processing.service.PaymentSystemAllService;
import ru.edme.processing.util.TestData;

import java.util.List;

@SpringBootTest
class PaymentSystemSpringAllServiceImplTest extends PostgreSQLContainerInitializer {

    @Autowired
    private PaymentSystemAllService paymentSystemAllService;

    @Test
    void saveShouldCreateObjectTest() {
        TestData testData = new TestData();
        PaymentSystemDto paymentSystemDto = testData.paymentSystemDto;
        long expected = 1;

        PaymentSystemDto paymentSystemSaved = paymentSystemAllService.save(paymentSystemDto);
        long actual = paymentSystemSaved.getId();

        Assertions.assertTrue(expected <= actual);
    }

    @Test
    void findByIdShouldReturnObjectTest() {
        PaymentSystemDto actual = paymentSystemAllService.findById(1L);

        Assertions.assertNotNull(actual);
    }

    @Test
    void findByIdShouldReturnEmptyObjectTest() {
        Assertions.assertThrows(RuntimeException.class, () -> paymentSystemAllService.findById(0L));
    }

    @Test
    void findAllShouldReturnListSize() {
        int expected = 1;
        List<PaymentSystemDto> paymentSystems = paymentSystemAllService.findAll();
        int actual = paymentSystems.size();

        Assertions.assertTrue(expected <= actual);
    }

    @Test
    void updateTest() {
        PaymentSystemDto actual = paymentSystemAllService.findById(1L);
        actual.setPaymentSystemName("ZZZZZZZZZZZ");

        paymentSystemAllService.update(actual);
        PaymentSystemDto expected = paymentSystemAllService.findById(1L);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteShouldReturnTrueTest() {
        boolean actual = paymentSystemAllService.delete(1L);
        boolean expected = true;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteShouldReturnRuntimeExceptionTest() {
        paymentSystemAllService.delete(1L);

        Assertions.assertThrows(RuntimeException.class, () -> paymentSystemAllService.findById(1L));
    }
}