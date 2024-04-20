package com.ilgrig.tuum.service;

import com.ilgrig.tuum.converter.AccountConverter;
import com.ilgrig.tuum.domain.Account;
import com.ilgrig.tuum.domain.Balance;
import com.ilgrig.tuum.mapper.AccountMapper;
import com.ilgrig.tuum.mapper.BalanceMapper;
import com.ilgrig.tuum.model.account.CreationAccountDTO;
import com.ilgrig.tuum.model.account.ResponseAccountDTO;
import com.ilgrig.tuum.util.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountConverter accountConverter;
    private final AccountMapper accountMapper;
    private final BalanceMapper balanceMapper;

    @Override
    public ResponseAccountDTO get(final Long id) {
        Account account = accountMapper.findAccountById(id)
                .orElseThrow(() -> new AccountNotFoundException("No account found with ID: " + id));
        Set<Balance> balances = accountMapper.findBalancesByAccountId(id);
        account.setBalances(balances);
        return accountConverter.accountToResponseAccountDTO(account);
    }


    @Transactional
    @Override
    public ResponseAccountDTO create(final CreationAccountDTO creationAccountDTO) {
        final Account account = accountConverter.creationAccountDTOToAccount(creationAccountDTO);
        accountMapper.insert(account);
        accountConverter.fillBalances(account, creationAccountDTO);
        for (Balance balance : account.getBalances()) {
            balance.setAccount(account);
            balanceMapper.insert(balance);
        }
        return accountConverter.accountToResponseAccountDTO(account);
    }

}
