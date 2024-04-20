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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionMapper transactionMapper;
    private final BalanceMapper balanceMapper;
    private final TransactionConverter transactionConverter;

    @Transactional
    @Override
    public ResponseTransactionDTO createTransaction(CreationTransactionDTO dto) {
        Balance balance = findBalanceByAccountIdAndCurrency(dto.getAccountId(), dto.getCurrency());
        if (dto.getDirection() == TransactionDirection.OUT && balance.getAvailableAmount().compareTo(dto.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds for the transaction.");
        }

        Transaction transaction = transactionConverter.toTransaction(dto);
        transaction.setBalanceId(balance.getId());

        updateBalance(balance, dto.getAmount(), dto.getDirection());
        transactionMapper.insert(transaction);
        return transactionConverter.toResponseDTO(transaction);
    }

    @Transactional
    @Override
    public void updateBalance(Balance balance, BigDecimal amount, TransactionDirection direction) {
        BigDecimal newAmount = direction == TransactionDirection.IN ? balance.getAvailableAmount().add(amount) : balance.getAvailableAmount().subtract(amount);
        balance.setAvailableAmount(newAmount);
        balance.setLastUpdated(OffsetDateTime.now());
        balanceMapper.update(balance);
    }

    private Balance findBalanceByAccountIdAndCurrency(Long accountId, String currency) {
        Balance balance = balanceMapper.findBalanceByAccountIdAndCurrency(accountId, currency);
        if (balance == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Balance not found for account and currency.");
        }
        return balance;
    }
}
