package com.ilgrig.tuum.service;

import com.ilgrig.tuum.converter.BalanceConverter;
import com.ilgrig.tuum.domain.Balance;
import com.ilgrig.tuum.mapper.BalanceMapper;
import com.ilgrig.tuum.model.balance.CreationBalanceDTO;
import com.ilgrig.tuum.model.balance.ResponseBalanceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

import com.ilgrig.tuum.util.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(NotFoundException::new);
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

}
