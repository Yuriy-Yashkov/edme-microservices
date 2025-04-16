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
import ru.edme.dto.requestDTO.CardRequestDTO;
import ru.edme.dto.responseDTO.CardResponseDTO;
import ru.edme.mapper.CardMapper;
import ru.edme.model.Card;
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
        CardRequestDTO cardRequestDTO = testData.cardRequestDTO;
        Card card = cardMapper.toCard(cardRequestDTO);

        String jsonRequest = objectMapper.writeValueAsString(cardRequestDTO);

        Mockito.when(cardAllService.save(any(CardRequestDTO.class))).thenReturn(card);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(cardRequestDTO.getId()))
                .andExpect(jsonPath("$.cardNumber").value(cardRequestDTO.getCardNumber()));

        Mockito.verify(cardAllService, times(1)).save(any(CardRequestDTO.class));
    }

    @Test
    void findById() throws Exception {
        CardResponseDTO cardResponseDTOId = testData.cardResponseDTOId;
        Long id = cardResponseDTOId.getId();

        Mockito.when(cardAllService.findById(id)).thenReturn(cardResponseDTOId);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cardResponseDTOId.getId()))
                .andExpect(jsonPath("$.cardNumber").value(cardResponseDTOId.getCardNumber()));

        Mockito.verify(cardAllService, times(1)).findById(id);
    }

    @Test
    void findAll() throws Exception {
        List<CardResponseDTO> cards = List.of(testData.cardResponseDTOId, testData.cardResponseDTOId);

        Mockito.when(cardAllService.findAll()).thenReturn(cards);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(cards.size()));

        Mockito.verify(cardAllService, times(1)).findAll();
    }

    @Test
    void update() throws Exception {
        CardRequestDTO cardRequestDTOId = testData.cardRequestDTOId;
        Card card = cardMapper.toCard(cardRequestDTOId);
        String jsonRequest = objectMapper.writeValueAsString(cardRequestDTOId);

        Mockito.when(cardAllService.update(any(CardRequestDTO.class))).thenReturn(card);

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUpgradeRequired())
                .andExpect(jsonPath("$.id").value(cardRequestDTOId.getId()))
                .andExpect(jsonPath("$.cardNumber").value(cardRequestDTOId.getCardNumber()));

        Mockito.verify(cardAllService, times(1)).update(any(CardRequestDTO.class));
    }

    @Test
    void deleteShouldReturnOk() throws Exception {
        Long id = testData.cardRequestDTOId.getId();

        Mockito.when(cardAllService.delete(id)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/cards/{id}", id))
                .andExpect(status().isOk());

        Mockito.verify(cardAllService, times(1)).delete(id);
    }

    @Test
    void deleteShouldReturnNotFound() throws Exception {
        Long id = testData.cardRequestDTOId.getId();

        Mockito.when(cardAllService.delete(id)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/cards/{id}", id))
                .andExpect(status().isNotFound());

        Mockito.verify(cardAllService, times(1)).delete(id);
    }
}
