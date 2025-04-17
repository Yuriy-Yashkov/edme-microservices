package ru.edme.controller;

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
import ru.edme.dto.requestDto.PaymentSystemRequestDto;
import ru.edme.dto.responseDto.PaymentSystemResponseDto;
import ru.edme.mapper.PaymentSystemMapper;
import ru.edme.service.PaymentSystemAllService;
import ru.edme.util.TestData;

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
    private PaymentSystemAllService paymentSystemAllService;

    TestData testData = new TestData();

    @Test
    void create_ShouldReturnCreated() throws Exception {
        PaymentSystemResponseDto paymentSystemResponseDtoId = testData.paymentSystemResponseDtoId;
        String jsonRequest = objectMapper.writeValueAsString(paymentSystemResponseDtoId);

        Mockito.when(paymentSystemAllService.save(any(PaymentSystemRequestDto.class))).thenReturn(paymentSystemResponseDtoId);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/cards/payment-system")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(paymentSystemResponseDtoId.getId()))
                .andExpect(jsonPath("$.paymentSystemName").value(paymentSystemResponseDtoId.getPaymentSystemName()));

        Mockito.verify(paymentSystemAllService, times(1)).save(any(PaymentSystemRequestDto.class));
    }

    @Test
    void findById_ShouldReturnOk_WhenExists() throws Exception {
        PaymentSystemResponseDto paymentSystemResponseDtoId = testData.paymentSystemResponseDtoId;
        Long id = paymentSystemResponseDtoId.getId();

        Mockito.when(paymentSystemAllService.findById(id)).thenReturn(paymentSystemResponseDtoId);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards/payment-system/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(paymentSystemResponseDtoId.getId()))
                .andExpect(jsonPath("$.paymentSystemName").value(paymentSystemResponseDtoId.getPaymentSystemName()));

        Mockito.verify(paymentSystemAllService, times(1)).findById(id);
    }

    @Test
    void findAll_ShouldReturnOk() throws Exception {
        List<PaymentSystemResponseDto> paymentSystems = List.of(testData.paymentSystemResponseDtoId, testData.paymentSystemResponseDtoId);

        Mockito.when(paymentSystemAllService.findAll()).thenReturn(paymentSystems);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards/payment-system"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(paymentSystems.size()));

        Mockito.verify(paymentSystemAllService, times(1)).findAll();
    }

    @Test
    void update_ShouldReturnUpgradeRequired() throws Exception {
        PaymentSystemResponseDto paymentSystemResponseDtoId = testData.paymentSystemResponseDtoId;
        String jsonRequest = objectMapper.writeValueAsString(paymentSystemResponseDtoId);

        Mockito.when(paymentSystemAllService.update(any(PaymentSystemRequestDto.class))).thenReturn(paymentSystemResponseDtoId);

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/cards/payment-system")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUpgradeRequired())
                .andExpect(jsonPath("$.id").value(paymentSystemResponseDtoId.getId()))
                .andExpect(jsonPath("$.paymentSystemName").value(paymentSystemResponseDtoId.getPaymentSystemName()));

        Mockito.verify(paymentSystemAllService, times(1)).update(any(PaymentSystemRequestDto.class));
    }

    @Test
    void delete_ShouldReturnOk_WhenExists() throws Exception {
        Long id = testData.paymentSystemRequestDtoId.getId();

        Mockito.when(paymentSystemAllService.delete(id)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/cards/payment-system/{id}", id))
                .andExpect(status().isOk());

        Mockito.verify(paymentSystemAllService, times(1)).delete(id);
    }

    @Test
    void delete_ShouldReturnNotFound_WhenDoesNotExist() throws Exception {
        Long id = testData.paymentSystemRequestDtoId.getId();

        Mockito.when(paymentSystemAllService.delete(id)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/cards/payment-system/{id}", id))
                .andExpect(status().isNotFound());

        Mockito.verify(paymentSystemAllService, times(1)).delete(id);
    }
}
