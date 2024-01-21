package com.testing.piggybank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testing.piggybank.account.AccountController;
import com.testing.piggybank.account.AccountService;
import com.testing.piggybank.account.UpdateAccountRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    // Test for PUT endpoint to update account information
    @Test
    public void updateAccountTest() throws Exception {
        UpdateAccountRequest updateRequest = new UpdateAccountRequest();
        updateRequest.setAccountId(1L);
        updateRequest.setAccountName("New Account Name");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    // Test for GET endpoint to fetch account details
    @Test
    public void getAccountDetailsTest() throws Exception {
        Long accountId = 1L; // Example account ID
        // Here you would mock the service layer's response if needed
        // Mockito.when(accountService.getAccountById(accountId)).thenReturn(Optional.of(account));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts/" + accountId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        // Here you can add more assertions to check the content of the response
    }

    // Test for GET endpoint to fetch all accounts for a user
    @Test
    public void getAccountsByUserIdTest() throws Exception {
        Long userId = 1L; // Example user ID
        // Here you would mock the service layer's response if needed
        // Mockito.when(accountService.getAccountsByUserId(userId)).thenReturn(List.of(account));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/accounts")
                        .header("X-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        // Here you can add more assertions to check the content of the response
    }
}
