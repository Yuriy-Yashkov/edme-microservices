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
import ru.edme.processing.dto.IssuingBankDto;
import ru.edme.processing.exception.EntityNotFoundException;
import ru.edme.processing.service.IssuingBankAllService;
import ru.edme.processing.util.TestData;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@WithMockUser(roles = "ADMIN")
@WebMvcTest(IssuingBankController.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class IssuingBankControllerTest {

    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;

    @MockBean
    private IssuingBankAllService issuingBankAllService;

    TestData testData = new TestData();

    @Test
    void createShouldReturnCreated() throws Exception {
        IssuingBankDto issuingBankDto = testData.issuingBankDto;
        String jsonRequest = objectMapper.writeValueAsString(issuingBankDto);

        Mockito.when(issuingBankAllService.save(any(IssuingBankDto.class))).thenReturn(issuingBankDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/processing-center/issuing-banks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(issuingBankDto.getId()))
                .andExpect(jsonPath("$.abbreviatedName").value(issuingBankDto.getAbbreviatedName()));

        Mockito.verify(issuingBankAllService, times(1)).save(any(IssuingBankDto.class));
    }

    @Test
    void findByIdShouldReturnOk() throws Exception {
        IssuingBankDto issuingBankDto = testData.issuingBankDto;
        Long id = issuingBankDto.getId();

        Mockito.when(issuingBankAllService.findById(id)).thenReturn(issuingBankDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/processing-center/issuing-banks/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(issuingBankDto.getId()))
                .andExpect(jsonPath("$.abbreviatedName").value(issuingBankDto.getAbbreviatedName()));

        Mockito.verify(issuingBankAllService, times(1)).findById(id);
    }

    @Test
    void findByIdShouldReturnNotFound() throws Exception {
        Long id = 100L;
        Mockito.when(issuingBankAllService.findById(id)).thenThrow(new EntityNotFoundException("Банк-эмитент не найден"));

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/processing-center/issuing-banks/{id}", id))
                .andExpect(status().isNotFound());

        Mockito.verify(issuingBankAllService, times(1)).findById(id);
    }

    @Test
    void findAllShouldReturnOk() throws Exception {
        List<IssuingBankDto> banks = List.of(testData.issuingBankDto, testData.issuingBankDto);

        Mockito.when(issuingBankAllService.findAll()).thenReturn(banks);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/processing-center/issuing-banks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(banks.size()));

        Mockito.verify(issuingBankAllService, times(1)).findAll();
    }

    @Test
    void updateShouldReturnUpgradeRequired() throws Exception {
        IssuingBankDto issuingBankDto = testData.issuingBankDto;
        String jsonRequest = objectMapper.writeValueAsString(issuingBankDto);

        Mockito.when(issuingBankAllService.update(any(IssuingBankDto.class))).thenReturn(issuingBankDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/processing-center/issuing-banks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUpgradeRequired())
                .andExpect(jsonPath("$.id").value(issuingBankDto.getId()))
                .andExpect(jsonPath("$.abbreviatedName").value(issuingBankDto.getAbbreviatedName()));

        Mockito.verify(issuingBankAllService, times(1)).update(any(IssuingBankDto.class));
    }

    @Test
    void deleteShouldReturnOk() throws Exception {
        Long id = testData.issuingBankDto.getId();

        Mockito.when(issuingBankAllService.delete(id)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/processing-center/issuing-banks/{id}", id))
                .andExpect(status().isOk());

        Mockito.verify(issuingBankAllService, times(1)).delete(id);
    }

    @Test
    void deleteShouldReturnNotFound() throws Exception {
        Long id = testData.issuingBankDto.getId();

        Mockito.when(issuingBankAllService.delete(id)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/processing-center/issuing-banks/{id}", id))
                .andExpect(status().isNotFound());

        Mockito.verify(issuingBankAllService, times(1)).delete(id);
    }
}
