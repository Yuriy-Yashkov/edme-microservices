package ru.edme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.edme.model.IssuingBank;
import ru.edme.service.IssuingBankAllService;
import ru.edme.util.TestData;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IssuingBankController.class)
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class IssuingBankControllerTest {

    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;

    @MockBean
    private IssuingBankAllService issuingBankAllService;

    TestData testData = new TestData();

    @Test
    void createShouldReturnCreated() throws Exception {
        IssuingBank bank = testData.issuingBank;
        String jsonRequest = objectMapper.writeValueAsString(bank);

        Mockito.when(issuingBankAllService.save(any(IssuingBank.class))).thenReturn(bank);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/cards/accounts/issuing-banks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(bank.getId()))
                .andExpect(jsonPath("$.abbreviatedName").value(bank.getAbbreviatedName()));

        Mockito.verify(issuingBankAllService, times(1)).save(any(IssuingBank.class));
    }

    @Test
    void findByIdShouldReturnOk() throws Exception {
        IssuingBank bank = testData.issuingBank;
        Long id = bank.getId();

        Mockito.when(issuingBankAllService.findById(id)).thenReturn(bank);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards/accounts/issuing-banks/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bank.getId()))
                .andExpect(jsonPath("$.abbreviatedName").value(bank.getAbbreviatedName()));

        Mockito.verify(issuingBankAllService, times(1)).findById(id);
    }

    @Test
    void findByIdShouldReturnNotFound() throws Exception {
        Long id = 100L;
        Mockito.when(issuingBankAllService.findById(id)).thenThrow(new RuntimeException("Банк-эмитент не найден"));

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards/accounts/issuing-banks/{id}", id))
                .andExpect(status().isNotFound());

        Mockito.verify(issuingBankAllService, times(1)).findById(id);
    }

    @Test
    void findAllShouldReturnOk() throws Exception {
        List<IssuingBank> banks = List.of(testData.issuingBank, testData.issuingBankId);

        Mockito.when(issuingBankAllService.findAll()).thenReturn(banks);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards/accounts/issuing-banks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(banks.size()));

        Mockito.verify(issuingBankAllService, times(1)).findAll();
    }

    @Test
    void updateShouldReturnUpgradeRequired() throws Exception {
        IssuingBank updatedBank = testData.issuingBank;
        String jsonRequest = objectMapper.writeValueAsString(updatedBank);

        Mockito.when(issuingBankAllService.update(any(IssuingBank.class))).thenReturn(updatedBank);

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/cards/accounts/issuing-banks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUpgradeRequired())
                .andExpect(jsonPath("$.id").value(updatedBank.getId()))
                .andExpect(jsonPath("$.abbreviatedName").value(updatedBank.getAbbreviatedName()));

        Mockito.verify(issuingBankAllService, times(1)).update(any(IssuingBank.class));
    }

    @Test
    void deleteShouldReturnOk() throws Exception {
        Long id = testData.issuingBank.getId();

        Mockito.when(issuingBankAllService.delete(id)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/cards/accounts/issuing-banks/{id}", id))
                .andExpect(status().isOk());

        Mockito.verify(issuingBankAllService, times(1)).delete(id);
    }

    @Test
    void deleteShouldReturnNotFound() throws Exception {
        Long id = testData.issuingBank.getId();

        Mockito.when(issuingBankAllService.delete(id)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/cards/accounts/issuing-banks/{id}", id))
                .andExpect(status().isNotFound());

        Mockito.verify(issuingBankAllService, times(1)).delete(id);
    }
}
