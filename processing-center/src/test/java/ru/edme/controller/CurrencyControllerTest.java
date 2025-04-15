package ru.edme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.edme.model.Currency;
import ru.edme.service.CurrencyAllService;
import ru.edme.util.TestData;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
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
        Currency currency = testData.currency;
        String jsonRequest = objectMapper.writeValueAsString(currency);

        // Настроим мок CurrencyAllService
        Mockito.when(currencyAllService.save(any(Currency.class)))
                .thenReturn(currency);

        // ДЕЙСТВИЕ: Отправка POST-запроса
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/cards/accounts/currency")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                // ОЖИДАЕМЫЙ РЕЗУЛЬТАТ: HTTP 201 (Created) и JSON-ответ
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(currency.getId()))
                .andExpect(jsonPath("$.currencyName").value(currency.getCurrencyName()));

        // ПРОВЕРКА: Вызывался ли сервис `currencyAllService.save()`
        Mockito.verify(currencyAllService, times(1)).save(any(Currency.class));
    }

    @Test
    void findById() throws Exception {
        Currency currency = testData.currencyId;
        Long currencyId = currency.getId();

        Mockito.when(currencyAllService.findById(currencyId)).thenReturn(currency);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards/accounts/currency/{id}", currencyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(currency.getId()))
                .andExpect(jsonPath("$.currencyName").value(currency.getCurrencyName()));

        Mockito.verify(currencyAllService, times(1)).findById(currencyId);
    }

    @Test
    void findAll() throws Exception {
        List<Currency> currencies = List.of(testData.currencyId);
        Mockito.when(currencyAllService.findAll()).thenReturn(currencies);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards/accounts/currency")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(currencies.size())))
                .andExpect(jsonPath("$[0].id").value(currencies.get(0).getId()))
                .andExpect(jsonPath("$[0].currencyName").value(currencies.get(0).getCurrencyName()));

        Mockito.verify(currencyAllService, times(1)).findAll();
    }

    @Test
    void update() throws Exception {
        Currency currency = testData.currencyId;
        String jsonRequest = objectMapper.writeValueAsString(currency);

        Mockito.when(currencyAllService.update(any(Currency.class))).thenReturn(currency);

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/cards/accounts/currency")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUpgradeRequired())
                .andExpect(jsonPath("$.id").value(currency.getId()))
                .andExpect(jsonPath("$.currencyName").value(currency.getCurrencyName()));

        Mockito.verify(currencyAllService, times(1)).update(any(Currency.class));
    }

    @Test
    void delete() throws Exception {
        Long currencyId = 1L;
        Mockito.when(currencyAllService.delete(currencyId)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/cards/accounts/currency/{id}", currencyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(currencyAllService, times(1)).delete(currencyId);
    }
}