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
import ru.edme.dto.requestDto.CardRequestDto;
import ru.edme.dto.responseDto.CardResponseDto;
import ru.edme.mapper.CardMapper;
import ru.edme.service.CardAllService;
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
class CardControllerTest {

    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;
    private final CardMapper cardMapper;

    @MockBean
    private CardAllService cardAllService;

    TestData testData = new TestData();

    @Test
    void create() throws Exception {
        CardResponseDto cardResponseDtoId = testData.cardResponseDtoId;

        String jsonRequest = objectMapper.writeValueAsString(cardResponseDtoId);

        Mockito.when(cardAllService.save(any(CardRequestDto.class))).thenReturn(cardResponseDtoId);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(cardResponseDtoId.getId()))
                .andExpect(jsonPath("$.cardNumber").value(cardResponseDtoId.getCardNumber()));

        Mockito.verify(cardAllService, times(1)).save(any(CardRequestDto.class));
    }

    @Test
    void findById() throws Exception {
        CardResponseDto cardResponseDtoId = testData.cardResponseDtoId;
        Long id = cardResponseDtoId.getId();

        Mockito.when(cardAllService.findById(id)).thenReturn(cardResponseDtoId);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cardResponseDtoId.getId()))
                .andExpect(jsonPath("$.cardNumber").value(cardResponseDtoId.getCardNumber()));

        Mockito.verify(cardAllService, times(1)).findById(id);
    }

    @Test
    void findAll() throws Exception {
        List<CardResponseDto> cards = List.of(testData.cardResponseDtoId, testData.cardResponseDtoId);

        Mockito.when(cardAllService.findAll()).thenReturn(cards);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(cards.size()));

        Mockito.verify(cardAllService, times(1)).findAll();
    }

    @Test
    void update() throws Exception {
        CardResponseDto cardResponseDtoId = testData.cardResponseDtoId;
        String jsonRequest = objectMapper.writeValueAsString(cardResponseDtoId);

        Mockito.when(cardAllService.update(any(CardRequestDto.class))).thenReturn(cardResponseDtoId);

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUpgradeRequired())
                .andExpect(jsonPath("$.id").value(cardResponseDtoId.getId()))
                .andExpect(jsonPath("$.cardNumber").value(cardResponseDtoId.getCardNumber()));

        Mockito.verify(cardAllService, times(1)).update(any(CardRequestDto.class));
    }

    @Test
    void deleteShouldReturnOk() throws Exception {
        Long id = testData.cardRequestDtoId.getId();

        Mockito.when(cardAllService.delete(id)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/cards/{id}", id))
                .andExpect(status().isOk());

        Mockito.verify(cardAllService, times(1)).delete(id);
    }

    @Test
    void deleteShouldReturnNotFound() throws Exception {
        Long id = testData.cardRequestDtoId.getId();

        Mockito.when(cardAllService.delete(id)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/cards/{id}", id))
                .andExpect(status().isNotFound());

        Mockito.verify(cardAllService, times(1)).delete(id);
    }
}
