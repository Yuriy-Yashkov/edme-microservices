package ru.edme.processing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.edme.processing.dto.CurrencyDto;
import ru.edme.processing.service.CurrencyAllService;
import ru.edme.processing.util.TestData;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@WithMockUser(roles = "ADMIN")
@WebMvcTest(CurrencyController.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CurrencyControllerTest {

    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;

    @MockBean
    private CurrencyAllService currencyAllService;

    TestData testData = new TestData();

    @BeforeEach
    void setUp() {
    }

    @Test
    void create() throws Exception {
        // ДАНО: Входные данные (JSON-запрос)
        CurrencyDto currencyDto = testData.currencyDto;
        String jsonRequest = objectMapper.writeValueAsString(currencyDto);

        // Настроим мок CurrencyAllService
        Mockito.when(currencyAllService.save(any(CurrencyDto.class)))
                .thenReturn(currencyDto);

        // ДЕЙСТВИЕ: Отправка POST-запроса
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/processing-center/currency")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                // ОЖИДАЕМЫЙ РЕЗУЛЬТАТ: HTTP 201 (Created) и JSON-ответ
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(currencyDto.getId()))
                .andExpect(jsonPath("$.currencyName").value(currencyDto.getCurrencyName()));

        // ПРОВЕРКА: Вызывался ли сервис `currencyAllService.save()`
        Mockito.verify(currencyAllService, times(1)).save(any(CurrencyDto.class));
    }

    @Test
    void findById() throws Exception {
        CurrencyDto currencyDtoId = testData.currencyDtoId;
        Long currencyId = currencyDtoId.getId();

        Mockito.when(currencyAllService.findById(currencyId)).thenReturn(currencyDtoId);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/processing-center/currency/{id}", currencyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(currencyDtoId.getId()))
                .andExpect(jsonPath("$.currencyName").value(currencyDtoId.getCurrencyName()));

        Mockito.verify(currencyAllService, times(1)).findById(currencyId);
    }

    @Test
    void findAll() throws Exception {
        List<CurrencyDto> currencies = List.of(testData.currencyDtoId);
        Mockito.when(currencyAllService.findAll()).thenReturn(currencies);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/processing-center/currency")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(currencies.size())))
                .andExpect(jsonPath("$[0].id").value(currencies.get(0).getId()))
                .andExpect(jsonPath("$[0].currencyName").value(currencies.get(0).getCurrencyName()));

        Mockito.verify(currencyAllService, times(1)).findAll();
    }

    @Test
    void update() throws Exception {
        CurrencyDto currencyDtoId = testData.currencyDtoId;
        String jsonRequest = objectMapper.writeValueAsString(currencyDtoId);

        Mockito.when(currencyAllService.update(any(CurrencyDto.class))).thenReturn(currencyDtoId);

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/processing-center/currency")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUpgradeRequired())
                .andExpect(jsonPath("$.id").value(currencyDtoId.getId()))
                .andExpect(jsonPath("$.currencyName").value(currencyDtoId.getCurrencyName()));

        Mockito.verify(currencyAllService, times(1)).update(any(CurrencyDto.class));
    }

    @Test
    void delete() throws Exception {
        Long currencyId = 1L;
        Mockito.when(currencyAllService.delete(currencyId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/processing-center/currency/{id}", currencyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(currencyAllService, times(1)).delete(currencyId);
    }
}