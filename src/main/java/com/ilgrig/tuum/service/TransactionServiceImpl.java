package com.ilgrig.tuum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl {

//    private final TransactionConverter transactionConverter;
//    private final TransactionMapper transactionMapper;
//
//
//    @Override
//    public List<CreationTransactionDTO> findAll(RowBounds rowBounds) {
//        final List<Transaction> transactions = transactionMapper.findAll(rowBounds);
//        return transactions.stream()
//                .map(transactionConverter::toTransactionDTO)
//                .toList();
//    }
//
//    @Override
//    public CreationTransactionDTO get(final Long id) {
//        return transactionMapper.findById(id)
//                .map(transactionConverter::toTransactionDTO)
//                .orElseThrow(NotFoundException::new);
//    }
//
//    @Override
//    public Long create(final CreationTransactionDTO creationTransactionDTO) {
//        final Transaction transaction = transactionConverter.fromTransactionDTO(creationTransactionDTO);
//        transaction.setDateCreated(OffsetDateTime.now());
//        transaction.setLastUpdated(OffsetDateTime.now());
//        transactionMapper.insert(transaction);
//        return transaction.getId();
//    }

}
