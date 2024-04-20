package com.ilgrig.tuum.service;

import com.ilgrig.tuum.domain.Balance;
import com.ilgrig.tuum.model.transaction.CreationTransactionDTO;
import com.ilgrig.tuum.model.transaction.ResponseTransactionDTO;
import com.ilgrig.tuum.model.transaction.TransactionDirection;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.List;


public interface TransactionService {

    List<ResponseTransactionDTO> findAll(Long accountId, RowBounds rowBounds);

    CreationTransactionDTO get(Long id);

    ResponseTransactionDTO create(CreationTransactionDTO creationTransactionDTO);

    ResponseTransactionDTO createTransaction(CreationTransactionDTO dto);

    void updateBalance(Balance balance, BigDecimal amount, TransactionDirection direction);
}
