package ru.edme.sales.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import ru.edme.sales.PostgreSQLContainerInitializer;
import ru.edme.sales.dto.requestDto.PaymentSystemRequestDto;
import ru.edme.sales.dto.responseDto.PaymentSystemResponseDto;
import ru.edme.sales.mapper.PaymentSystemMapper;
import ru.edme.sales.service.PaymentSystemService;
import ru.edme.sales.util.TestData;

import java.util.List;

@SpringBootTest
@WithMockUser(username = "yriy", password = "123", authorities = "ADMIN")
class PaymentSystemSpringAllServiceImplTest extends PostgreSQLContainerInitializer {

    @Autowired
    private PaymentSystemService paymentSystemService;

    @Autowired
    private PaymentSystemMapper paymentSystemMapper;

    @Test
    void saveShouldCreateObjectTest() {
        TestData testData = new TestData();
        PaymentSystemRequestDto paymentSystem = testData.paymentSystemRequestDto;
        long expected = 1;

        PaymentSystemResponseDto paymentSystemSaved = paymentSystemService.save(paymentSystem);
        long actual = paymentSystemSaved.getId();

        Assertions.assertTrue(expected <= actual);
    }

    @Test
    void findByIdShouldReturnObjectTest() {
        PaymentSystemResponseDto actual = paymentSystemService.findById(1L);

        Assertions.assertNotNull(actual);
    }

    @Test
    void findByIdShouldReturnEmptyObjectTest() {
        Assertions.assertThrows(RuntimeException.class, () -> paymentSystemService.findById(0L));
    }

    @Test
    void findAllShouldReturnListSize() {
        int expected = 1;
        List<PaymentSystemResponseDto> paymentSystems = paymentSystemService.findAllWrapped().getValues();
        int actual = paymentSystems.size();

        Assertions.assertTrue(expected <= actual);
    }

    @Test
    void updateTest() {
        PaymentSystemResponseDto actual = paymentSystemService.findById(1L);
        actual.setPaymentSystemName("ZZZZZZZZZZZ");
        PaymentSystemRequestDto paymentSystemRequestDto = paymentSystemMapper.toPaymentSystemRequestDto(actual);

        paymentSystemService.update(paymentSystemRequestDto);
        PaymentSystemResponseDto expected = paymentSystemService.findById(1L);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteShouldReturnTrueTest() {
        boolean actual = paymentSystemService.delete(1L);
        boolean expected = true;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteShouldReturnRuntimeExceptionTest() {
        paymentSystemService.delete(1L);

        Assertions.assertThrows(RuntimeException.class, () -> paymentSystemService.findById(1L));
    }
}