package com.ilgrig.tuum.integration.service;

import com.ilgrig.tuum.converter.TransactionConverter;
import com.ilgrig.tuum.domain.Account;
import com.ilgrig.tuum.domain.Balance;
import com.ilgrig.tuum.domain.Transaction;
import com.ilgrig.tuum.mapper.BalanceMapper;
import com.ilgrig.tuum.mapper.TransactionMapper;
import com.ilgrig.tuum.model.transaction.CreationTransactionDTO;
import com.ilgrig.tuum.model.transaction.ResponseTransactionDTO;
import com.ilgrig.tuum.model.transaction.TransactionDirection;
import com.ilgrig.tuum.service.TransactionServiceImpl;
import com.ilgrig.tuum.util.MessagePublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
public class TransactionServiceImplTests {

    @Autowired
    private TransactionServiceImpl transactionService;

    @MockBean
    private TransactionMapper transactionMapper;

    @MockBean
    private BalanceMapper balanceMapper;

    @MockBean
    private TransactionConverter transactionConverter;

    @MockBean
    private MessagePublisher messagePublisher;

    @Test
    public void testCreateTransaction_Success() {
        Account account = new Account();
        account.setId(1L);
        account.setCustomerId(1L);
        account.setCountry("US");

        CreationTransactionDTO dto = new CreationTransactionDTO();
        dto.setAccountId(1L);
        dto.setAmount(new BigDecimal("30.00"));
        dto.setCurrency("USD");
        dto.setDirection(TransactionDirection.IN);

        Balance balance = new Balance();
        balance.setId(1L);
        balance.setAccount(account);
        balance.setAvailableAmount(new BigDecimal("20.00"));
        balance.setCurrency("USD");

        Transaction transaction = new Transaction();
        ResponseTransactionDTO expectedResponse = new ResponseTransactionDTO();
        expectedResponse.setTransactionId(1L); // Example setter

        when(balanceMapper.findBalanceByAccountIdAndCurrency(1L, "USD")).thenReturn(Optional.of(balance));
        when(transactionConverter.toTransaction(dto)).thenReturn(transaction);
        doNothing().when(transactionMapper).insert(any(Transaction.class));
        when(transactionConverter.toResponseDTO(transaction)).thenReturn(expectedResponse);

        ResponseTransactionDTO response = transactionService.createTransaction(dto);

        assertNotNull(response, "Response should not be null");
        assertEquals(expectedResponse, response, "Expected response did not match actual response");
        verify(transactionMapper).insert(any(Transaction.class));
        verify(messagePublisher).publishTransactionCreated(any(Transaction.class));
    }

    @Test
    public void testCreateTransaction_InsufficientFunds() {
        CreationTransactionDTO dto = new CreationTransactionDTO();
        dto.setAccountId(1L);
        dto.setAmount(new BigDecimal("100.00"));
        dto.setCurrency("USD");
        dto.setDirection(TransactionDirection.OUT);


        Balance balance = new Balance();
        balance.setAvailableAmount(new BigDecimal("50.00"));
        balance.setCurrency("USD");

        when(balanceMapper.findBalanceByAccountIdAndCurrency(1L, "USD")).thenReturn(java.util.Optional.of(balance));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            transactionService.createTransaction(dto);
        });

        assertEquals("Insufficient funds for the transaction.", exception.getMessage());
    }

    @Test
    public void testCreateTransaction_AccountNotFound() {
        CreationTransactionDTO dto = new CreationTransactionDTO();
        dto.setAccountId(9999L); // Non-existing account ID
        dto.setAmount(new BigDecimal("100.00"));
        dto.setCurrency("USD");
        dto.setDirection(TransactionDirection.IN);

        when(balanceMapper.findBalanceByAccountIdAndCurrency(9999L, "USD")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            transactionService.createTransaction(dto);
        });

        assertEquals("Account not found with ID 9999", exception.getMessage());
    }

}
