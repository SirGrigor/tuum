package com.ilgrig.tuum.service;

import com.ilgrig.tuum.converter.TransactionConverter;
import com.ilgrig.tuum.domain.Transaction;
import com.ilgrig.tuum.mapper.TransactionMapper;
import com.ilgrig.tuum.model.TransactionDTO;
import com.ilgrig.tuum.util.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionConverter transactionConverter;
    private final TransactionMapper transactionMapper;


    @Override
    public List<TransactionDTO> findAll(RowBounds rowBounds) {
        final List<Transaction> transactions = transactionMapper.findAll(rowBounds);
        return transactions.stream()
                .map(transactionConverter::toTransactionDTO)
                .toList();
    }

    @Override
    public TransactionDTO get(final Long id) {
        return transactionMapper.findById(id)
                .map(transactionConverter::toTransactionDTO)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Long create(final TransactionDTO transactionDTO) {
        final Transaction transaction = transactionConverter.fromTransactionDTO(transactionDTO);
        transaction.setDateCreated(OffsetDateTime.now());
        transaction.setLastUpdated(OffsetDateTime.now());
        transactionMapper.insert(transaction);
        return transaction.getId();
    }

    @Override
    public void update(final Long id, final TransactionDTO transactionDTO) {
        Transaction transaction = transactionConverter.fromTransactionDTO(transactionDTO);
        transaction.setLastUpdated(OffsetDateTime.now());
        transactionMapper.update(transaction);
    }

    @Override
    public boolean existsByCurrencyCode(String code) {
        return false;
    }

}
