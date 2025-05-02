package ru.edme.sales.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.edme.sales.dto.requestDto.PaymentSystemRequestDto;
import ru.edme.sales.dto.responseDto.PaymentSystemResponseDto;
import ru.edme.sales.mapper.PaymentSystemMapper;
import ru.edme.sales.service.PaymentSystemService;
import ru.edme.sales.util.ListWrapper;
import ru.edme.sales.util.TestData;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@WithMockUser(username = "yriy", password = "123", authorities = "ADMIN")
class PaymentSystemControllerTest {

    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;
    private final PaymentSystemMapper paymentSystemMapper;

    @MockBean
    private PaymentSystemService paymentSystemService;

    TestData testData = new TestData();

    @Test
    void create_ShouldReturnCreated() throws Exception {
        PaymentSystemResponseDto paymentSystemResponseDtoId = testData.paymentSystemResponseDtoId;
        String jsonRequest = objectMapper.writeValueAsString(paymentSystemResponseDtoId);

        Mockito.when(paymentSystemService.save(any(PaymentSystemRequestDto.class))).thenReturn(paymentSystemResponseDtoId);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/sales-point/payment-system")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(paymentSystemResponseDtoId.getId()))
                .andExpect(jsonPath("$.paymentSystemName").value(paymentSystemResponseDtoId.getPaymentSystemName()));

        Mockito.verify(paymentSystemService, times(1)).save(any(PaymentSystemRequestDto.class));
    }

    @Test
    void findById_ShouldReturnOk_WhenExists() throws Exception {
        PaymentSystemResponseDto paymentSystemResponseDtoId = testData.paymentSystemResponseDtoId;
        Long id = paymentSystemResponseDtoId.getId();

        Mockito.when(paymentSystemService.findById(id)).thenReturn(paymentSystemResponseDtoId);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/sales-point/payment-system/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(paymentSystemResponseDtoId.getId()))
                .andExpect(jsonPath("$.paymentSystemName").value(paymentSystemResponseDtoId.getPaymentSystemName()));

        Mockito.verify(paymentSystemService, times(1)).findById(id);
    }

    @Test
    void findAll_ShouldReturnOk() throws Exception {
        List<PaymentSystemResponseDto> paymentSystems = List.of(testData.paymentSystemResponseDtoId, testData.paymentSystemResponseDtoId);
        ListWrapper<PaymentSystemResponseDto> listWrapper = new ListWrapper<>(paymentSystems);

        Mockito.when(paymentSystemService.findAllWrapped()).thenReturn(listWrapper);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/sales-point/payment-system"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(paymentSystems.size()));

        Mockito.verify(paymentSystemService, times(1)).findAllWrapped();
    }

    @Test
    void update_ShouldReturnUpgradeRequired() throws Exception {
        PaymentSystemResponseDto paymentSystemResponseDtoId = testData.paymentSystemResponseDtoId;
        String jsonRequest = objectMapper.writeValueAsString(paymentSystemResponseDtoId);

        Mockito.when(paymentSystemService.update(any(PaymentSystemRequestDto.class))).thenReturn(paymentSystemResponseDtoId);

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/sales-point/payment-system")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUpgradeRequired())
                .andExpect(jsonPath("$.id").value(paymentSystemResponseDtoId.getId()))
                .andExpect(jsonPath("$.paymentSystemName").value(paymentSystemResponseDtoId.getPaymentSystemName()));

        Mockito.verify(paymentSystemService, times(1)).update(any(PaymentSystemRequestDto.class));
    }

    @Test
    void delete_ShouldReturnOk_WhenExists() throws Exception {
        Long id = testData.paymentSystemRequestDtoId.getId();

        Mockito.when(paymentSystemService.delete(id)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/sales-point/payment-system/{id}", id))
                .andExpect(status().isOk());

        Mockito.verify(paymentSystemService, times(1)).delete(id);
    }

    @Test
    void delete_ShouldReturnNotFound_WhenDoesNotExist() throws Exception {
        Long id = testData.paymentSystemRequestDtoId.getId();

        Mockito.when(paymentSystemService.delete(id)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/sales-point/payment-system/{id}", id))
                .andExpect(status().isNotFound());

        Mockito.verify(paymentSystemService, times(1)).delete(id);
    }
}
