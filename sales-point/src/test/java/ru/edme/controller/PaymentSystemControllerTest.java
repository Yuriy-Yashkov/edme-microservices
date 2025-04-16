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
import ru.edme.dto.requestDTO.PaymentSystemRequestDTO;
import ru.edme.dto.responseDTO.PaymentSystemResponseDTO;
import ru.edme.mapper.PaymentSystemMapper;
import ru.edme.model.PaymentSystem;
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
        PaymentSystemRequestDTO paymentSystemRequestDTO = testData.paymentSystemRequestDTO;
        PaymentSystem paymentSystem = paymentSystemMapper.toPaymentSystem(paymentSystemRequestDTO);
        String jsonRequest = objectMapper.writeValueAsString(paymentSystemRequestDTO);

        Mockito.when(paymentSystemAllService.save(any(PaymentSystemRequestDTO.class))).thenReturn(paymentSystem);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/cards/payment-system")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(paymentSystemRequestDTO.getId()))
                .andExpect(jsonPath("$.paymentSystemName").value(paymentSystemRequestDTO.getPaymentSystemName()));

        Mockito.verify(paymentSystemAllService, times(1)).save(any(PaymentSystemRequestDTO.class));
    }

    @Test
    void findById_ShouldReturnOk_WhenExists() throws Exception {
        PaymentSystemResponseDTO paymentSystemResponseDTO = testData.paymentSystemResponseDTO;
        Long id = paymentSystemResponseDTO.getId();

        Mockito.when(paymentSystemAllService.findById(id)).thenReturn(paymentSystemResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards/payment-system/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(paymentSystemResponseDTO.getId()))
                .andExpect(jsonPath("$.paymentSystemName").value(paymentSystemResponseDTO.getPaymentSystemName()));

        Mockito.verify(paymentSystemAllService, times(1)).findById(id);
    }

    @Test
    void findAll_ShouldReturnOk() throws Exception {
        List<PaymentSystemResponseDTO> paymentSystems = List.of(testData.paymentSystemResponseDTOId, testData.paymentSystemResponseDTOId);

        Mockito.when(paymentSystemAllService.findAll()).thenReturn(paymentSystems);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards/payment-system"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(paymentSystems.size()));

        Mockito.verify(paymentSystemAllService, times(1)).findAll();
    }

    @Test
    void update_ShouldReturnUpgradeRequired() throws Exception {
        PaymentSystemRequestDTO paymentSystemRequestDTOId = testData.paymentSystemRequestDTOId;
        PaymentSystem paymentSystem = paymentSystemMapper.toPaymentSystem(paymentSystemRequestDTOId);
        String jsonRequest = objectMapper.writeValueAsString(paymentSystemRequestDTOId);

        Mockito.when(paymentSystemAllService.update(any(PaymentSystemRequestDTO.class))).thenReturn(paymentSystem);

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/cards/payment-system")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUpgradeRequired())
                .andExpect(jsonPath("$.id").value(paymentSystemRequestDTOId.getId()))
                .andExpect(jsonPath("$.paymentSystemName").value(paymentSystemRequestDTOId.getPaymentSystemName()));

        Mockito.verify(paymentSystemAllService, times(1)).update(any(PaymentSystemRequestDTO.class));
    }

    @Test
    void delete_ShouldReturnOk_WhenExists() throws Exception {
        Long id = testData.paymentSystemRequestDTOId.getId();

        Mockito.when(paymentSystemAllService.delete(id)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/cards/payment-system/{id}", id))
                .andExpect(status().isOk());

        Mockito.verify(paymentSystemAllService, times(1)).delete(id);
    }

    @Test
    void delete_ShouldReturnNotFound_WhenDoesNotExist() throws Exception {
        Long id = testData.paymentSystemRequestDTOId.getId();

        Mockito.when(paymentSystemAllService.delete(id)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/cards/payment-system/{id}", id))
                .andExpect(status().isNotFound());

        Mockito.verify(paymentSystemAllService, times(1)).delete(id);
    }
}
