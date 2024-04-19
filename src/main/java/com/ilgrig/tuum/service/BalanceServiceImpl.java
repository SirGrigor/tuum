package com.ilgrig.tuum.service;

import com.ilgrig.tuum.converter.BalanceConverter;
import com.ilgrig.tuum.domain.Balance;
import com.ilgrig.tuum.mapper.BalanceMapper;
import com.ilgrig.tuum.mapper.CurrencyMapper;
import com.ilgrig.tuum.model.BalanceDTO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

import com.ilgrig.tuum.util.NotFoundException;

@RequiredArgsConstructor
@Service
public class BalanceServiceImpl implements BalanceService {

    private final BalanceConverter balanceConverter;
    private final BalanceMapper balanceMapper;
    private final CurrencyMapper currencyMapper;


    @Override
    public List<BalanceDTO> findAll(final RowBounds rowBounds) {
        final List<Balance> balances = balanceMapper.findAll(rowBounds);
        return balances.stream()
                .map(balanceConverter::toBalanceDTO)
                .toList();
    }

    @Override
    public BalanceDTO get(final Long id) {
        return balanceMapper.findById(id)
                .map(balanceConverter::toBalanceDTO)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final BalanceDTO balanceDTO) {
        Balance balance = balanceConverter.fromBalanceDTO(balanceDTO);
        balance.setDateCreated(OffsetDateTime.now());
        balance.setLastUpdated(OffsetDateTime.now());
        balanceMapper.insert(balance);
        return balance.getId();
    }

    @Override
    public void update(final Long id, final BalanceDTO balanceDTO) {
        final Balance balance = balanceMapper.findById(id)
                .orElseThrow(NotFoundException::new);
        balanceConverter.fromBalanceDTO(balanceDTO);
        balanceMapper.update(balance);
    }

    @Override
    public boolean currencyExists(final String code) {
        return currencyMapper.existsByCode(code);
    }

}
