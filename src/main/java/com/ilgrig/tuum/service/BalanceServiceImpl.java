package com.ilgrig.tuum.service;

import com.ilgrig.tuum.converter.BalanceConverter;
import com.ilgrig.tuum.domain.Balance;
import com.ilgrig.tuum.mapper.BalanceMapper;
import com.ilgrig.tuum.model.balance.CreationBalanceDTO;
import com.ilgrig.tuum.model.balance.ResponseBalanceDTO;
import com.ilgrig.tuum.model.transaction.TransactionDirection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BalanceServiceImpl implements BalanceService {

    private final BalanceConverter balanceConverter;
    private final BalanceMapper balanceMapper;


    @Override
    public List<ResponseBalanceDTO> findAll() {
        final List<Balance> balances = balanceMapper.findAll();
        return balances.stream()
                .map(balanceConverter::toResponseBalanceDTO)
                .toList();
    }

    @Override
    public ResponseBalanceDTO get(final Long id) {
        return balanceMapper.findById(id)
                .map(balanceConverter::toResponseBalanceDTO)
                .orElseThrow(() -> new HttpClientErrorException(org.springframework.http.HttpStatus.NOT_FOUND));
    }

    @Transactional
    @Override
    public Long create(final CreationBalanceDTO creationBalanceDTO) {
        Balance balance = balanceConverter.toBalance(creationBalanceDTO);
        balance.setDateCreated(OffsetDateTime.now());
        balance.setLastUpdated(OffsetDateTime.now());
        balanceMapper.insert(balance);
        return balance.getId();
    }

    @Transactional
    @Override
    public void updateBalance(Balance balance, BigDecimal amount, TransactionDirection direction) {
        if (direction == TransactionDirection.IN) {
            balance.setAvailableAmount(balance.getAvailableAmount().add(amount));
        }

        if (direction == TransactionDirection.OUT) {
            balance.setAvailableAmount(balance.getAvailableAmount().subtract(amount));
        }

        balanceMapper.update(balance);
    }

    @Override
    public Balance findBalanceByAccountIdAndCurrency(Long accountId, String currency) {
        Balance balance = balanceMapper.findBalanceByAccountIdAndCurrency(accountId, currency);
        if (balance == null) {
            throw new HttpClientErrorException(org.springframework.http.HttpStatus.NOT_FOUND);
        }
        return balance;
    }
}
