package com.ilgrig.tuum.service;

import com.ilgrig.tuum.converter.AccountConverter;
import com.ilgrig.tuum.domain.Account;
import com.ilgrig.tuum.domain.Balance;
import com.ilgrig.tuum.mapper.AccountMapper;
import com.ilgrig.tuum.mapper.BalanceMapper;
import com.ilgrig.tuum.model.account.CreationAccountDTO;
import com.ilgrig.tuum.model.account.ResponseAccountDTO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;


@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountConverter accountConverter;
    private final AccountMapper accountMapper;
    private final BalanceMapper balanceMapper;

    @Override
    public ResponseAccountDTO get(final Long id) {
        return accountMapper.findById(id)
                .map(accountConverter::accountToResponseAccountDTO)
                .orElseThrow(() -> new HttpClientErrorException(org.springframework.http.HttpStatus.NOT_FOUND));
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
