package com.ilgrig.tuum.service;

import com.ilgrig.tuum.model.transaction.CreationTransactionDTO;
import org.apache.ibatis.session.RowBounds;

import java.util.List;


public interface TransactionService {

    List<CreationTransactionDTO> findAll(RowBounds rowBounds);

    CreationTransactionDTO get(Long id);

    Long create(CreationTransactionDTO creationTransactionDTO);

}
