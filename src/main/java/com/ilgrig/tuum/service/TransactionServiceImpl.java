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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
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

        messagePublisher.publishEvent("Transaction Created", Map.of(
                "accountId", transaction.getAccountId(),
                "transactionId", transaction.getId(),
                "amount", transaction.getAmount(),
                "currency", transaction.getCurrency(),
                "direction", transaction.getDirection().toString(),
                "balanceAfter", transaction.getBalanceAfterTransaction()
        ));

        return transactionConverter.toResponseDTO(transaction);
    }

    @Transactional
    @Override
    public BigDecimal updateBalance(Balance balance, BigDecimal amount, TransactionDirection direction) {
        BigDecimal newAmount = direction == TransactionDirection.IN ? balance.getAvailableAmount().add(amount) : balance.getAvailableAmount().subtract(amount);
        balance.setAvailableAmount(newAmount);
        balance.setLastUpdated(OffsetDateTime.now());
        balanceMapper.update(balance);

        messagePublisher.publishEvent("Balance Updated", Map.of(
                "balanceId", balance.getId(),
                "direction",direction,
                "newBalance", balance.getAvailableAmount(),
                "currency", balance.getCurrency()
        ));

        return balance.getAvailableAmount();
    }

    @Override
    public List<ResponseTransactionDTO> findAllByAccountId(Long accountId, RowBounds rowBounds) {
        List<Transaction> transactions = transactionMapper.findAllByAccountId(accountId, rowBounds);
        return transactions.stream()
                .map(transactionConverter::toResponseDTO)
                .collect(Collectors.toList());
    }

    private Balance findBalanceByAccountIdAndCurrency(Long accountId, String currency) {
        Balance balance = balanceMapper.findBalanceByAccountIdAndCurrency(accountId, currency);
        if (balance == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Balance not found for account and currency.");
        }
        return balance;
    }
}
