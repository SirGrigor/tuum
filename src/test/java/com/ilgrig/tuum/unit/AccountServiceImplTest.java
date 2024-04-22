package com.ilgrig.tuum.unit;

import com.ilgrig.tuum.converter.AccountConverter;
import com.ilgrig.tuum.domain.Account;
import com.ilgrig.tuum.domain.Balance;
import com.ilgrig.tuum.mapper.AccountMapper;
import com.ilgrig.tuum.mapper.BalanceMapper;
import com.ilgrig.tuum.model.account.CreationAccountDTO;
import com.ilgrig.tuum.model.account.ResponseAccountDTO;
import com.ilgrig.tuum.service.AccountServiceImpl;
import com.ilgrig.tuum.util.AccountNotFoundException;
import com.ilgrig.tuum.util.MessagePublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountConverter accountConverter;
    @Mock
    private AccountMapper accountMapper;
    @Mock
    private BalanceMapper balanceMapper;
    @Mock
    private MessagePublisher messagePublisher;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void shouldCreateAccountSuccessfully() {
        CreationAccountDTO dto = new CreationAccountDTO();
        dto.setCustomerId(1L);
        dto.setCountry("SE");
        dto.setCurrencies(Arrays.asList("SEK", "USD", "EUR"));

        Account account = new Account();
        account.setCustomerId(dto.getCustomerId());
        account.setCountry(dto.getCountry());

        when(accountConverter.creationAccountDTOToAccount(dto)).thenReturn(account);

        ResponseAccountDTO responseDTO = new ResponseAccountDTO();
        responseDTO.setAccountId(1L);
        responseDTO.setCustomerId(dto.getCustomerId());
        when(accountConverter.accountToResponseAccountDTO(account)).thenReturn(responseDTO);

        ResponseAccountDTO result = accountService.create(dto);

        verify(accountMapper).insert(account);
        ArgumentCaptor<Balance> balanceCaptor = ArgumentCaptor.forClass(Balance.class);
        verify(messagePublisher).publishAccountCreated(account, dto.getCurrencies());
        assertEquals(dto.getCustomerId(), result.getCustomerId());
        assertEquals(1L, result.getAccountId());
        assertNotNull(result);

        List<Balance> insertedBalances = balanceCaptor.getAllValues();
        assertTrue(insertedBalances.stream().allMatch(b -> dto.getCurrencies().contains(b.getCurrency())));
    }


    @Test
    void shouldFailToCreateAccountWhenConverterThrowsException() {
        CreationAccountDTO dto = new CreationAccountDTO();
        when(accountConverter.creationAccountDTOToAccount(dto)).thenThrow(new RuntimeException("Conversion failed"));

        assertThrows(RuntimeException.class, () -> accountService.create(dto));

        verify(accountMapper, never()).insert(any(Account.class));
        verify(messagePublisher, never()).publishAccountCreated(any(Account.class), anyList());
    }

    @Test
    void shouldRetrieveAccountSuccessfully() {
        Long accountId = 1L;
        Account account = new Account();
        Set<Balance> balances = new HashSet<>();
        when(accountMapper.findAccountById(accountId)).thenReturn(Optional.of(account));
        when(balanceMapper.findBalancesByAccountId(accountId)).thenReturn(balances);
        when(accountConverter.accountToResponseAccountDTO(account)).thenReturn(new ResponseAccountDTO());

        ResponseAccountDTO result = accountService.get(accountId);

        assertThat(result).isNotNull();
        verify(accountMapper).findAccountById(accountId);
        verify(balanceMapper).findBalancesByAccountId(accountId);
    }

    @Test
    void shouldThrowAccountNotFoundException() {
        Long accountId = 1L;
        when(accountMapper.findAccountById(accountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.get(accountId));
    }

    @Test
    void shouldConfirmAccountExists() {
        Long accountId = 1L;
        when(accountMapper.findAccountById(accountId)).thenReturn(Optional.of(new Account()));

        boolean exists = accountService.validateAccountExistence(accountId);

        assertThat(exists).isTrue();
    }

    @Test
    void shouldConfirmAccountDoesNotExist() {
        Long accountId = 1L;
        when(accountMapper.findAccountById(accountId)).thenReturn(Optional.empty());

        boolean exists = accountService.validateAccountExistence(accountId);

        assertThat(exists).isFalse();
    }

    @Test
    void shouldRollbackTransactionIfPublishingFails() {
        CreationAccountDTO dto = new CreationAccountDTO();
        Account account = new Account();
        when(accountConverter.creationAccountDTOToAccount(dto)).thenReturn(account);
        doNothing().when(accountMapper).insert(account);
        doThrow(new RuntimeException("Messaging service down")).when(messagePublisher).publishAccountCreated(any(Account.class), anyList());

        assertThrows(RuntimeException.class, () -> accountService.create(dto));

        verify(balanceMapper, never()).insert(any(Balance.class));
    }

}

