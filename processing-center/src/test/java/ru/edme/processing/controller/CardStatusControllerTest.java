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
import ru.edme.processing.dto.CardStatusDto;
import ru.edme.processing.exception.EntityNotFoundException;
import ru.edme.processing.service.CardStatusAllService;
import ru.edme.processing.util.TestData;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@WithMockUser(roles = "ADMIN")
@WebMvcTest(CardStatusController.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CardStatusControllerTest {

    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;

    @MockBean
    private CardStatusAllService cardStatusAllService;

    TestData testData = new TestData();

    @Test
    void create_ShouldReturnCreatedStatus() throws Exception {
        CardStatusDto cardStatusDto = new CardStatusDto(1L, "ACTIVE");
        String jsonRequest = objectMapper.writeValueAsString(cardStatusDto);

        when(cardStatusAllService.save(any(CardStatusDto.class))).thenReturn(cardStatusDto);

        mockMvc.perform(post("/v1/processing-center/card-statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(cardStatusDto.getId()))
                .andExpect(jsonPath("$.cardStatusName").value(cardStatusDto.getCardStatusName()));
    }

    @Test
    void findById_ShouldReturnCardStatus_WhenExists() throws Exception {
        CardStatusDto cardStatusDtoId = testData.cardStatusDtoId;
        Long id = cardStatusDtoId.getId();

        Mockito.when(cardStatusAllService.findById(id)).thenReturn(cardStatusDtoId);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/processing-center/card-statuses/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cardStatusDtoId.getId()))
                .andExpect(jsonPath("$.cardStatusName").value(cardStatusDtoId.getCardStatusName()));

        Mockito.verify(cardStatusAllService, times(1)).findById(id);
    }

    @Test
    void findById_ShouldReturnNotFound_WhenDoesNotExist() throws Exception {
        when(cardStatusAllService.findById(anyLong())).thenThrow(new EntityNotFoundException("Статус не найден"));

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/processing-center/card-statuses/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAll_ShouldReturnListOfCardStatuses() throws Exception {
        List<CardStatusDto> statuses = List.of(testData.cardStatusDtoId, testData.cardStatusDtoId);

        when(cardStatusAllService.findAll()).thenReturn(statuses);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/processing-center/card-statuses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(statuses.size()));
    }

    @Test
    void update_ShouldReturnUpgradeRequired() throws Exception {
        CardStatusDto cardStatusDtoId = testData.cardStatusDtoId;

        when(cardStatusAllService.update(any(CardStatusDto.class))).thenReturn(cardStatusDtoId);

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/processing-center/card-statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardStatusDtoId)))
                .andExpect(status().isUpgradeRequired())
                .andExpect(jsonPath("$.id").value(cardStatusDtoId.getId()))
                .andExpect(jsonPath("$.cardStatusName").value(cardStatusDtoId.getCardStatusName()));
    }

    @Test
    void delete_ShouldReturnOk_WhenExists() throws Exception {
        when(cardStatusAllService.delete(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/processing-center/card-statuses/1"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_ShouldReturnNotFound_WhenDoesNotExist() throws Exception {
        when(cardStatusAllService.delete(100L)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/processing-center/card-statuses/100"))
                .andExpect(status().isNotFound());
    }
}