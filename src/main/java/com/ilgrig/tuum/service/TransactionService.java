package com.ilgrig.tuum.service;

import com.ilgrig.tuum.domain.Balance;
import com.ilgrig.tuum.model.transaction.CreationTransactionDTO;
import com.ilgrig.tuum.model.transaction.ResponseTransactionDTO;
import com.ilgrig.tuum.model.transaction.TransactionDirection;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.List;


public interface TransactionService {

    ResponseTransactionDTO createTransaction(CreationTransactionDTO dto);

    BigDecimal updateBalance(Balance balance, BigDecimal amount, TransactionDirection direction);

    List<ResponseTransactionDTO> findAllByAccountId(Long accountId, RowBounds rowBounds);
}
