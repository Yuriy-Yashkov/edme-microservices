package ru.edme.processing.controller;

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
import ru.edme.processing.dto.AccountDto;
import ru.edme.processing.service.AccountAllService;
import ru.edme.processing.util.TestData;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class AccountControllerTest {

    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;

    @MockBean
    private AccountAllService accountAllService;

    TestData testData = new TestData();

    @Test
    void create() throws Exception {
        AccountDto accountDtoId = testData.accountDtoId;
        String jsonRequest = objectMapper.writeValueAsString(accountDtoId);

        Mockito.when(accountAllService.save(any(AccountDto.class))).thenReturn(accountDtoId);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/processing-center/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(accountDtoId.getId()))
                .andExpect(jsonPath("$.accountNumber").value(accountDtoId.getAccountNumber()));

        Mockito.verify(accountAllService, times(1)).save(any(AccountDto.class));
    }

    @Test
    void findById() throws Exception {
        AccountDto accountDtoId = testData.accountDtoId;
        Long id = accountDtoId.getId();

        Mockito.when(accountAllService.findById(id)).thenReturn(accountDtoId);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/processing-center/accounts/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(accountDtoId.getId()))
                .andExpect(jsonPath("$.accountNumber").value(accountDtoId.getAccountNumber()));

        Mockito.verify(accountAllService, times(1)).findById(id);
    }

    @Test
    void findAll() throws Exception {
        List<AccountDto> accounts = List.of(testData.accountDtoId, testData.accountDtoId);

        Mockito.when(accountAllService.findAll()).thenReturn(accounts);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/processing-center/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(accounts.size()));

        Mockito.verify(accountAllService, times(1)).findAll();
    }

    @Test
    void update() throws Exception {
        AccountDto accountDtoId = testData.accountDtoId;
        String jsonRequest = objectMapper.writeValueAsString(accountDtoId);

        Mockito.when(accountAllService.update(any(AccountDto.class))).thenReturn(accountDtoId);

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/processing-center/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUpgradeRequired())
                .andExpect(jsonPath("$.id").value(accountDtoId.getId()))
                .andExpect(jsonPath("$.accountNumber").value(accountDtoId.getAccountNumber()));

        Mockito.verify(accountAllService, times(1)).update(any(AccountDto.class));
    }

    @Test
    void deleteShouldReturnOk() throws Exception {
        Long id = testData.accountDtoId.getId();

        Mockito.when(accountAllService.delete(id)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/processing-center/accounts/{id}", id))
                .andExpect(status().isOk());

        Mockito.verify(accountAllService, times(1)).delete(id);
    }

    @Test
    void deleteShouldReturnNotFound() throws Exception {
        Long id = testData.accountDtoId.getId();

        Mockito.when(accountAllService.delete(id)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/processing-center/accounts/{id}", id))
                .andExpect(status().isNotFound());

        Mockito.verify(accountAllService, times(1)).delete(id);
    }
}
