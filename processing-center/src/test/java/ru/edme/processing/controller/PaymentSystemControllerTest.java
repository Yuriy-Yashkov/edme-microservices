package ru.edme.processing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.edme.processing.dto.PaymentSystemDto;
import ru.edme.processing.service.PaymentSystemAllService;
import ru.edme.processing.util.TestData;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@WithMockUser(roles = "ADMIN")
@WebMvcTest(PaymentSystemController.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class PaymentSystemControllerTest {

    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;

    @MockBean
    private PaymentSystemAllService paymentSystemAllService;

    TestData testData = new TestData();

    @Test
    void create_ShouldReturnCreated() throws Exception {
        PaymentSystemDto paymentSystemDto = testData.paymentSystemDto;
        String jsonRequest = objectMapper.writeValueAsString(paymentSystemDto);

        Mockito.when(paymentSystemAllService.save(any(PaymentSystemDto.class))).thenReturn(paymentSystemDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/processing-center/payment-system")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(paymentSystemDto.getId()))
                .andExpect(jsonPath("$.paymentSystemName").value(paymentSystemDto.getPaymentSystemName()));

        Mockito.verify(paymentSystemAllService, times(1)).save(any(PaymentSystemDto.class));
    }

    @Test
    void findById_ShouldReturnOk_WhenExists() throws Exception {
        PaymentSystemDto paymentSystemDto = testData.paymentSystemDto;
        Long id = paymentSystemDto.getId();

        Mockito.when(paymentSystemAllService.findById(id)).thenReturn(paymentSystemDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/processing-center/payment-system/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(paymentSystemDto.getId()))
                .andExpect(jsonPath("$.paymentSystemName").value(paymentSystemDto.getPaymentSystemName()));

        Mockito.verify(paymentSystemAllService, times(1)).findById(id);
    }

    @Test
    void findAll_ShouldReturnOk() throws Exception {
        List<PaymentSystemDto> paymentSystems = List.of(testData.paymentSystemDto, testData.paymentSystemDto);

        Mockito.when(paymentSystemAllService.findAll()).thenReturn(paymentSystems);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/processing-center/payment-system"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(paymentSystems.size()));

        Mockito.verify(paymentSystemAllService, times(1)).findAll();
    }

    @Test
    void update_ShouldReturnUpgradeRequired() throws Exception {
        PaymentSystemDto paymentSystemDto = testData.paymentSystemDto;
        String jsonRequest = objectMapper.writeValueAsString(paymentSystemDto);

        Mockito.when(paymentSystemAllService.update(any(PaymentSystemDto.class))).thenReturn(paymentSystemDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/processing-center/payment-system")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUpgradeRequired())
                .andExpect(jsonPath("$.id").value(paymentSystemDto.getId()))
                .andExpect(jsonPath("$.paymentSystemName").value(paymentSystemDto.getPaymentSystemName()));

        Mockito.verify(paymentSystemAllService, times(1)).update(any(PaymentSystemDto.class));
    }

    @Test
    void delete_ShouldReturnOk_WhenExists() throws Exception {
        Long id = testData.paymentSystemDto.getId();

        Mockito.when(paymentSystemAllService.delete(id)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/processing-center/payment-system/{id}", id))
                .andExpect(status().isOk());

        Mockito.verify(paymentSystemAllService, times(1)).delete(id);
    }

    @Test
    void delete_ShouldReturnNotFound_WhenDoesNotExist() throws Exception {
        Long id = testData.paymentSystemDto.getId();

        Mockito.when(paymentSystemAllService.delete(id)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/processing-center/payment-system/{id}", id))
                .andExpect(status().isNotFound());

        Mockito.verify(paymentSystemAllService, times(1)).delete(id);
    }
}
