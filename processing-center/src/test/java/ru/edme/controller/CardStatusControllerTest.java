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
import ru.edme.model.CardStatus;
import ru.edme.service.CardStatusAllService;
import ru.edme.util.TestData;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardStatusController.class)
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CardStatusControllerTest {

    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;

    @MockBean
    private CardStatusAllService cardStatusAllService;

    TestData testData = new TestData();

    @Test
    void create_ShouldReturnCreatedStatus() throws Exception {
        CardStatus status = new CardStatus(1L, "ACTIVE");
        String jsonRequest = objectMapper.writeValueAsString(status);

        when(cardStatusAllService.save(any(CardStatus.class))).thenReturn(status);

        mockMvc.perform(post("/v1/cards/card-statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(status.getId()))
                .andExpect(jsonPath("$.cardStatusName").value(status.getCardStatusName()));
    }

    @Test
    void findById_ShouldReturnCardStatus_WhenExists() throws Exception {
        CardStatus status = testData.cardStatusId;
        Long id = status.getId();

        Mockito.when(cardStatusAllService.findById(id)).thenReturn(status);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards/card-statuses/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(status.getId()))
                .andExpect(jsonPath("$.cardStatusName").value(status.getCardStatusName()));

        Mockito.verify(cardStatusAllService, times(1)).findById(id);
    }

    @Test
    void findById_ShouldReturnNotFound_WhenDoesNotExist() throws Exception {
        when(cardStatusAllService.findById(anyLong())).thenThrow(new RuntimeException("Статус не найден"));

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards/card-statuses/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAll_ShouldReturnListOfCardStatuses() throws Exception {
        List<CardStatus> statuses = List.of(testData.cardStatusId, testData.cardStatus);

        when(cardStatusAllService.findAll()).thenReturn(statuses);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards/card-statuses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(statuses.size()));
    }

    @Test
    void update_ShouldReturnUpgradeRequired() throws Exception {
        CardStatus status = testData.cardStatusId;

        when(cardStatusAllService.update(any(CardStatus.class))).thenReturn(status);

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/cards/card-statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(status)))
                .andExpect(status().isUpgradeRequired())
                .andExpect(jsonPath("$.id").value(status.getId()))
                .andExpect(jsonPath("$.cardStatusName").value(status.getCardStatusName()));
    }

    @Test
    void delete_ShouldReturnOk_WhenExists() throws Exception {
        when(cardStatusAllService.delete(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/cards/card-statuses/1"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_ShouldReturnNotFound_WhenDoesNotExist() throws Exception {
        when(cardStatusAllService.delete(100L)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/cards/card-statuses/100"))
                .andExpect(status().isNotFound());
    }
}