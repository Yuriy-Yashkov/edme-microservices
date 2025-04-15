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
import ru.edme.model.Account;
import ru.edme.service.AccountAllService;
import ru.edme.util.TestData;

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
        Account account = testData.accountId;
        String jsonRequest = objectMapper.writeValueAsString(account);

        Mockito.when(accountAllService.save(any(Account.class))).thenReturn(account);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/cards/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(account.getId()))
                .andExpect(jsonPath("$.accountNumber").value(account.getAccountNumber()));

        Mockito.verify(accountAllService, times(1)).save(any(Account.class));
    }

    @Test
    void findById() throws Exception {
        Account account = testData.accountId;
        Long id = account.getId();

        Mockito.when(accountAllService.findById(id)).thenReturn(account);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards/accounts/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(account.getId()))
                .andExpect(jsonPath("$.accountNumber").value(account.getAccountNumber()));

        Mockito.verify(accountAllService, times(1)).findById(id);
    }

    @Test
    void findAll() throws Exception {
        List<Account> accounts = List.of(testData.accountId, testData.account);

        Mockito.when(accountAllService.findAll()).thenReturn(accounts);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(accounts.size()));

        Mockito.verify(accountAllService, times(1)).findAll();
    }

    @Test
    void update() throws Exception {
        Account updatedAccount = testData.accountId;
        String jsonRequest = objectMapper.writeValueAsString(updatedAccount);

        Mockito.when(accountAllService.update(any(Account.class))).thenReturn(updatedAccount);

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/cards/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUpgradeRequired())
                .andExpect(jsonPath("$.id").value(updatedAccount.getId()))
                .andExpect(jsonPath("$.accountNumber").value(updatedAccount.getAccountNumber()));

        Mockito.verify(accountAllService, times(1)).update(any(Account.class));
    }

    @Test
    void deleteShouldReturnOk() throws Exception {
        Long id = testData.accountId.getId();

        Mockito.when(accountAllService.delete(id)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/cards/accounts/{id}", id))
                .andExpect(status().isOk());

        Mockito.verify(accountAllService, times(1)).delete(id);
    }

    @Test
    void deleteShouldReturnNotFound() throws Exception {
        Long id = testData.accountId.getId();

        Mockito.when(accountAllService.delete(id)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/cards/accounts/{id}", id))
                .andExpect(status().isNotFound());

        Mockito.verify(accountAllService, times(1)).delete(id);
    }
}
