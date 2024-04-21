package com.ilgrig.tuum.service;

import com.ilgrig.tuum.converter.AccountConverter;
import com.ilgrig.tuum.domain.Account;
import com.ilgrig.tuum.mapper.AccountMapper;
import com.ilgrig.tuum.mapper.BalanceMapper;
import com.ilgrig.tuum.model.account.CreationAccountDTO;
import com.ilgrig.tuum.model.account.ResponseAccountDTO;
import com.ilgrig.tuum.util.AccountNotFoundException;
import com.ilgrig.tuum.util.MessagePublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AccountServiceImplTests {

    @MockBean
    private AccountMapper accountMapper;

    @MockBean
    private BalanceMapper balanceMapper;

    @MockBean
    private AccountConverter accountConverter;

    @MockBean
    private MessagePublisher messagePublisher;

    @Autowired
    private AccountServiceImpl accountService;

    @Test
    public void testCreateAccount() {
        CreationAccountDTO dto = new CreationAccountDTO();
        dto.setCustomerId(1L);
        dto.setCountry("US");
        dto.setCurrencies(List.of("USD", "EUR"));

        Account account = new Account();
        when(accountConverter.creationAccountDTOToAccount(dto)).thenReturn(account);

        doNothing().when(accountMapper).insert(any(Account.class));

        ResponseAccountDTO expectedResponse = new ResponseAccountDTO();
        when(accountConverter.accountToResponseAccountDTO(any(Account.class))).thenReturn(expectedResponse);

        ResponseAccountDTO response = accountService.create(dto);

        assertNotNull(response, "The response should not be null");
        assertEquals(expectedResponse, response, "The expected response should match the actual response");

        verify(accountMapper).insert(any(Account.class));

        verify(messagePublisher).publishAccountCreated(any(Account.class), anyList());
    }

    @Test
    public void testGetAccount_NotFound() {
        when(accountMapper.findAccountById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(AccountNotFoundException.class, () -> {
            accountService.get(999L);
        });

        assertTrue(exception.getMessage().contains("No account found with ID: 999"));
    }
}
