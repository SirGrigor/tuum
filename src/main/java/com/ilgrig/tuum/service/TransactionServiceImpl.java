package com.ilgrig.tuum.service;

import com.ilgrig.tuum.converter.TransactionConverter;
import com.ilgrig.tuum.domain.Balance;
import com.ilgrig.tuum.domain.Transaction;
import com.ilgrig.tuum.mapper.BalanceMapper;
import com.ilgrig.tuum.mapper.TransactionMapper;
import com.ilgrig.tuum.model.transaction.CreationTransactionDTO;
import com.ilgrig.tuum.model.transaction.ResponseTransactionDTO;
import com.ilgrig.tuum.model.transaction.TransactionDirection;
import com.ilgrig.tuum.util.InsufficientFundsException;
import com.ilgrig.tuum.util.MessagePublisher;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionMapper transactionMapper;
    private final BalanceMapper balanceMapper;
    private final TransactionConverter transactionConverter;
    private final MessagePublisher messagePublisher;

    @Transactional
    @Override
    public ResponseTransactionDTO createTransaction(CreationTransactionDTO dto) {
        Balance balance = findBalanceByAccountIdAndCurrency(dto.getAccountId(), dto.getCurrency());
        if (dto.getDirection() == TransactionDirection.OUT && balance.getAvailableAmount().compareTo(dto.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds for the transaction.");
        }
        Transaction transaction = transactionConverter.toTransaction(dto);
        transaction.setBalanceId(balance.getId());
        transaction.setBalanceAfterTransaction(updateBalance(balance, dto.getAmount(), dto.getDirection()));
        transactionMapper.insert(transaction);
        messagePublisher.publishTransactionCreated(transaction);
        return transactionConverter.toResponseDTO(transaction);
    }

    public BigDecimal updateBalance(Balance balance, BigDecimal amount, TransactionDirection direction) {
        BigDecimal newAmount = direction == TransactionDirection.IN ? balance.getAvailableAmount().add(amount) : balance.getAvailableAmount().subtract(amount);
        balance.setAvailableAmount(newAmount);
        balanceMapper.update(balance);
        messagePublisher.publishBalanceUpdated(balance);
        return newAmount;
    }

    @Override
    public List<ResponseTransactionDTO> findAllByAccountId(Long accountId, RowBounds rowBounds) {
        List<Transaction> transactions = transactionMapper.findAllByAccountId(accountId, rowBounds);
        return transactions.stream()
                .map(transactionConverter::toResponseDTO)
                .toList();
    }

    private Balance findBalanceByAccountIdAndCurrency(Long accountId, String currency) {
        Balance balance = balanceMapper.findBalanceByAccountIdAndCurrency(accountId, currency);
        if (balance == null) {
            throw new InsufficientFundsException("Balance not found for account and currency.");
        }
        return balance;
    }
}