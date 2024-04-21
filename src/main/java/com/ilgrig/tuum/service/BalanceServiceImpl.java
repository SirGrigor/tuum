package com.ilgrig.tuum.service;

import com.ilgrig.tuum.converter.BalanceConverter;
import com.ilgrig.tuum.domain.Balance;
import com.ilgrig.tuum.mapper.BalanceMapper;
import com.ilgrig.tuum.model.balance.CreationBalanceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@RequiredArgsConstructor
@Service
public class BalanceServiceImpl implements BalanceService {

    private final BalanceConverter balanceConverter;
    private final BalanceMapper balanceMapper;

    @Transactional
    @Override
    public Long create(final CreationBalanceDTO creationBalanceDTO) {
        Balance balance = balanceConverter.toBalance(creationBalanceDTO);
        balance.setDateCreated(OffsetDateTime.now());
        balance.setLastUpdated(OffsetDateTime.now());
        balanceMapper.insert(balance);
        return balance.getId();
    }

}
