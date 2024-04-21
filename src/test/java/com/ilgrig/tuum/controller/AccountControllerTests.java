package com.ilgrig.tuum.controller;

import com.ilgrig.tuum.config.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerTests extends BaseIntegrationTest {

    @Test
    public void testCreateAccount_Success() throws Exception {
        String jsonContent = "{\"customerId\": 999, \"country\": \"DE\", \"currencies\": [\"EUR\"]}";
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.customerId").value(999));
    }

    @Test
    public void testCreateAccount_Bad_Request() throws Exception {
        String jsonContent = "{\"customerId\": 1, \"country\": \"US\", \"currencies\": [\"USD\"]}";
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error.message").value("Account already exists for customer id"));
    }

    @Test
    public void testCreateAccount_ValidationFailure() throws Exception {
        String jsonContent = "{\"customerId\": \"\", \"country\": \"XX\", \"currencies\": []}";
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").isNotEmpty());
    }

    @Test
    public void testCreateAccount_InvalidCurrency() throws Exception {
        String jsonContent = "{\"customerId\": 4, \"country\": \"US\", \"currencies\": [\"CAD\"]}";
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value(containsString("Validation failed: createAccount.creationAccountDTO.currencies: Invalid currency")));
    }

    @Test
    public void testGetAccount_NotFound() throws Exception {
        mockMvc.perform(get("/api/accounts/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.message").value("No account found with ID: 9999"));
    }

    @Test
    public void testGetAccount_Success() throws Exception {
        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accountId").value(1));
    }

    @Test
    public void createAccount_withValidData_returnsAccountInfo() throws Exception {
        String jsonContent = "{\"customerId\": 123, \"country\": \"US\", \"currencies\": [\"USD\", \"EUR\"]}";
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.customerId").value(123));
    }

    @Test
    public void createAccount_withInvalidCurrency_returnsBadRequest() throws Exception {
        String jsonContent = "{\"customerId\": 456, \"country\": \"UK\", \"currencies\": [\"USD\", \"JPY\"]}";
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.message").value(containsString("Validation failed: createAccount.creationAccountDTO.currencies: Invalid currency,")))
                .andExpect(jsonPath("$.error.message").value(containsString("createAccount.creationAccountDTO.country: Invalid country. Must be a valid ISO country code.")));
    }

    @Test
    public void getAccount_existingAccountId_returnsAccountInfo() throws Exception {
        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accountId").value(1));
    }

    @Test
    public void getAccount_nonExistingAccountId_returnsNotFound() throws Exception {
        mockMvc.perform(get("/api/accounts/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.message").value("No account found with ID: 999"));
    }

}
