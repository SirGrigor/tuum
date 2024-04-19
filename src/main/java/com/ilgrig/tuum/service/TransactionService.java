package com.ilgrig.tuum.service;

import com.ilgrig.tuum.model.TransactionDTO;
import org.apache.ibatis.session.RowBounds;

import java.util.List;


public interface TransactionService {

    List<TransactionDTO> findAll(RowBounds rowBounds);

    TransactionDTO get(Long id);

    Long create(TransactionDTO transactionDTO);

    void update(Long id, TransactionDTO transactionDTO);

    boolean existsByCurrencyCode(String code);
}
