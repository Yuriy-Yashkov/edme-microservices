package ru.edme.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import ru.edme.dto.CardDto;
import ru.edme.service.CardAllService;
import ru.edme.util.TestData;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CardControllerTest {

    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;

    @MockBean
    private CardAllService cardAllService;

    TestData testData = new TestData();

    @Test
    void testt() throws JsonProcessingException {
        LocalDate date = LocalDate.of(2025, 3, 30);
        String json = objectMapper.writeValueAsString(date);
        System.out.println(json);  // Должно быть "2025-03-30"

    }

    @Test
    void create() throws Exception {
        CardDto cardDtoId = testData.cardDtoId;
        String jsonRequest = objectMapper.writeValueAsString(cardDtoId);

        Mockito.when(cardAllService.save(any(CardDto.class))).thenReturn(cardDtoId);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(cardDtoId.getId()))
                .andExpect(jsonPath("$.cardNumber").value(cardDtoId.getCardNumber()));

        Mockito.verify(cardAllService, times(1)).save(any(CardDto.class));
    }

    @Test
    void findById() throws Exception {
        CardDto cardDtoId = testData.cardDtoId;
        Long id = cardDtoId.getId();

        Mockito.when(cardAllService.findById(id)).thenReturn(cardDtoId);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cardDtoId.getId()))
                .andExpect(jsonPath("$.cardNumber").value(cardDtoId.getCardNumber()));

        Mockito.verify(cardAllService, times(1)).findById(id);
    }

    @Test
    void findAll() throws Exception {
        List<CardDto> cards = List.of(testData.cardDtoId, testData.cardDtoId);

        Mockito.when(cardAllService.findAll()).thenReturn(cards);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(cards.size()));

        Mockito.verify(cardAllService, times(1)).findAll();
    }

    @Test
    void update() throws Exception {
        CardDto cardDtoId = testData.cardDtoId;
        String jsonRequest = objectMapper.writeValueAsString(cardDtoId);

        Mockito.when(cardAllService.update(any(CardDto.class))).thenReturn(cardDtoId);

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUpgradeRequired())
                .andExpect(jsonPath("$.id").value(cardDtoId.getId()))
                .andExpect(jsonPath("$.cardNumber").value(cardDtoId.getCardNumber()));

        Mockito.verify(cardAllService, times(1)).update(any(CardDto.class));
    }

    @Test
    void deleteShouldReturnOk() throws Exception {
        Long id = testData.cardDtoId.getId();

        Mockito.when(cardAllService.delete(id)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/cards/{id}", id))
                .andExpect(status().isOk());

        Mockito.verify(cardAllService, times(1)).delete(id);
    }

    @Test
    void deleteShouldReturnNotFound() throws Exception {
        Long id = testData.cardDtoId.getId();

        Mockito.when(cardAllService.delete(id)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/cards/{id}", id))
                .andExpect(status().isNotFound());

        Mockito.verify(cardAllService, times(1)).delete(id);
    }
}
