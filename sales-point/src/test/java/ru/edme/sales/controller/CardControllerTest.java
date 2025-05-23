package ru.edme.sales.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.edme.sales.dto.requestDto.CardRequestDto;
import ru.edme.sales.dto.responseDto.CardResponseDto;
import ru.edme.sales.service.CardService;
import ru.edme.sales.service.CardTransferService;
import ru.edme.sales.util.ListWrapper;
import ru.edme.sales.util.TestData;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "yriy", password = "123", roles = "ADMIN")
class CardControllerTest {

    @Autowired
    private  ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService cardService;

    @MockBean
    private CardTransferService cardTransferService;

    TestData testData = new TestData();

    @Test
    void create() throws Exception {
        CardResponseDto cardResponseDtoId = testData.cardResponseDtoId;

        String jsonRequest = objectMapper.writeValueAsString(cardResponseDtoId);

        Mockito.when(cardService.save(any(CardRequestDto.class))).thenReturn(cardResponseDtoId);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/sales-point/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(cardResponseDtoId.getId()))
                .andExpect(jsonPath("$.cardNumber").value(cardResponseDtoId.getCardNumber()));

        Mockito.verify(cardService, times(1)).save(any(CardRequestDto.class));
    }

    @Test
    void findById() throws Exception {
        CardResponseDto cardResponseDtoId = testData.cardResponseDtoId;
        Long id = cardResponseDtoId.getId();

        Mockito.when(cardService.findById(id)).thenReturn(cardResponseDtoId);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/sales-point/cards/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cardResponseDtoId.getId()))
                .andExpect(jsonPath("$.cardNumber").value(cardResponseDtoId.getCardNumber()));

        Mockito.verify(cardService, times(1)).findById(id);
    }

    @Test
    void findAll() throws Exception {
        List<CardResponseDto> cards = List.of(testData.cardResponseDtoId, testData.cardResponseDtoId);
        ListWrapper<CardResponseDto> listWrapper = new ListWrapper<>(cards);

        Mockito.when(cardService.findAllWrapped()).thenReturn(listWrapper);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/sales-point/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(cards.size()));

        Mockito.verify(cardService, times(1)).findAllWrapped();
    }

    @Test
    void update() throws Exception {
        CardResponseDto cardResponseDtoId = testData.cardResponseDtoId;
        String jsonRequest = objectMapper.writeValueAsString(cardResponseDtoId);

        Mockito.when(cardService.update(any(CardRequestDto.class))).thenReturn(cardResponseDtoId);

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/sales-point/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUpgradeRequired())
                .andExpect(jsonPath("$.id").value(cardResponseDtoId.getId()))
                .andExpect(jsonPath("$.cardNumber").value(cardResponseDtoId.getCardNumber()));

        Mockito.verify(cardService, times(1)).update(any(CardRequestDto.class));
    }

    @Test
    void deleteShouldReturnOk() throws Exception {
        Long id = testData.cardRequestDtoId.getId();

        Mockito.when(cardService.delete(id)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/sales-point/cards/{id}", id))
                .andExpect(status().isOk());

        Mockito.verify(cardService, times(1)).delete(id);
    }

    @Test
    void deleteShouldReturnNotFound() throws Exception {
        Long id = testData.cardRequestDtoId.getId();

        Mockito.when(cardService.delete(id)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/sales-point/cards/{id}", id))
                .andExpect(status().isNotFound());

        Mockito.verify(cardService, times(1)).delete(id);
    }
}
