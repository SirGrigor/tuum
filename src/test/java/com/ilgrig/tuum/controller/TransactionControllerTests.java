package com.ilgrig.tuum.controller;

import com.ilgrig.tuum.config.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransactionControllerTests extends BaseIntegrationTest {

    @Test
    public void testCreateTransaction_Success() throws Exception {
        String jsonContent = "{\"accountId\": 1, \"amount\": \"50.55\", \"currency\": \"USD\", \"direction\": \"IN\", \"description\": \"Deposit\"}";
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.amount").value("50.55"));
    }

    @Test
    public void testCreateTransaction_InsufficientFunds() throws Exception {
        String jsonContent = "{\"accountId\": 1, \"amount\": \"10000.00\", \"currency\": \"USD\", \"direction\": \"OUT\", \"description\": \"Large Withdrawal\"}";
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error.message").value("Insufficient funds for the transaction."));
    }

    @Test
    public void testCreateTransaction_AccountNotFound() throws Exception {
        String jsonContent = "{\"accountId\": 9999, \"amount\": \"100.00\", \"currency\": \"USD\", \"direction\": \"IN\", \"description\": \"Deposit\"}";
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.message").value("Account with ID 9999 does not exist."));
    }

    @Test
    public void testGetTransactionsByAccountId() throws Exception {
        mockMvc.perform(get("/api/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].accountId").value(1));
    }
}
