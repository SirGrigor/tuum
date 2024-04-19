package com.ilgrig.tuum.service;

import com.ilgrig.tuum.converter.AccountConverter;
import com.ilgrig.tuum.domain.Account;
import com.ilgrig.tuum.domain.Balance;
import com.ilgrig.tuum.mapper.AccountMapper;
import com.ilgrig.tuum.mapper.CountryMapper;
import com.ilgrig.tuum.mapper.CurrencyMapper;
import com.ilgrig.tuum.model.account.CreationAccountDTO;
import com.ilgrig.tuum.model.account.ResponseAccountDTO;
import com.ilgrig.tuum.util.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;


@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountConverter accountConverter;
    private final AccountMapper accountMapper;
    private final CountryMapper countryMapper;
    private final CurrencyMapper currencyMapper;

    @Override
    public ResponseAccountDTO get(final Long id) {
        return accountMapper.findById(id)
                .map(accountConverter::accountToResponseAccountDTO)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public ResponseAccountDTO create(final CreationAccountDTO creationAccountDTO) {
        Account account = accountConverter.creationAccountDTOToAccount(creationAccountDTO);
        accountMapper.insert(account);
        return accountConverter.accountToResponseAccountDTO(account);
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
