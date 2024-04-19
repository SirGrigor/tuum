package com.ilgrig.tuum.service;

import com.ilgrig.tuum.converter.AccountConverter;
import com.ilgrig.tuum.domain.Account;
import com.ilgrig.tuum.mapper.AccountMapper;
import com.ilgrig.tuum.mapper.CountryMapper;
import com.ilgrig.tuum.mapper.CurrencyMapper;
import com.ilgrig.tuum.model.AccountDTO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.ilgrig.tuum.util.NotFoundException;


@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountConverter accountConverter;
    private final AccountMapper accountMapper;
    private final CountryMapper countryMapper;
    private final CurrencyMapper currencyMapper;

    @Override
    public List<AccountDTO> findAll(RowBounds rowBounds) {
        List<Account> accounts = accountMapper.findAll(rowBounds);
        return accounts.stream()
                .map(accountConverter::toAccountDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDTO get(final Long id) {
        return accountMapper.findById(id)
                .map(accountConverter::toAccountDTO)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final AccountDTO accountDTO) {
        Account account = accountConverter.fromAccountDTO(accountDTO);
        account.setDateCreated(OffsetDateTime.now());
        account.setLastUpdated(OffsetDateTime.now());
        accountMapper.insert(account);
        return account.getId();
    }

    @Override
    public void update(final Long id, final AccountDTO accountDTO) {
        final Account account = accountMapper.findById(id)
                .orElseThrow(NotFoundException::new);
        accountConverter.fromAccountDTO(accountDTO);
        accountMapper.insert(account);
    }

    @Override
    public boolean countryExists(final String code) {
        return countryMapper.existsByCode(code);
    }

    @Override
    public boolean currencyExists(String code) {
        return currencyMapper.existsByCode(code);
    }

}
