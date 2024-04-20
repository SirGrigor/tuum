package com.ilgrig.tuum.service;

import com.ilgrig.tuum.domain.Balance;
import com.ilgrig.tuum.model.balance.CreationBalanceDTO;
import com.ilgrig.tuum.model.balance.ResponseBalanceDTO;
import com.ilgrig.tuum.model.transaction.TransactionDirection;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.List;


public interface BalanceService {

    List<ResponseBalanceDTO> findAll();

    ResponseBalanceDTO get(Long id);

    Long create(CreationBalanceDTO creationBalanceDTO);

//    void updateBalance(Balance balance, BigDecimal amount, TransactionDirection direction);

    Balance findBalanceByAccountIdAndCurrency(Long accountId, String currency);
}
