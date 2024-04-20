package com.ilgrig.tuum.service;

import com.ilgrig.tuum.domain.Balance;
import com.ilgrig.tuum.domain.Transaction;
import com.ilgrig.tuum.mapper.BalanceMapper;
import com.ilgrig.tuum.mapper.TransactionMapper;
import com.ilgrig.tuum.model.transaction.CreationTransactionDTO;
import com.ilgrig.tuum.model.transaction.ResponseTransactionDTO;
import com.ilgrig.tuum.model.transaction.TransactionDirection;
import com.ilgrig.tuum.util.GlobalExceptionHandler;
import com.ilgrig.tuum.util.InsufficientFundsException;
import com.ilgrig.tuum.util.LoggingUtility;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionMapper transactionMapper;
    private final BalanceMapper balanceMapper;

    @Transactional
    public ResponseTransactionDTO createTransaction(CreationTransactionDTO dto) {
        Balance balance = findBalanceByAccountIdAndCurrency(dto.getAccountId(), dto.getCurrency());

        LoggingUtility.LOGGER.info("Found balance: {}", balance.toString());
        if (dto.getDirection() == TransactionDirection.OUT) {
            checkSufficientFunds(balance.getAvailableAmount(), dto.getAmount());
        }

        updateBalance(balance, dto.getAmount(), dto.getDirection());
        Transaction transaction = createTransactionRecord(dto, balance);
        LoggingUtility.LOGGER.info("Creating transaction: {}", transaction.toString());
        transactionMapper.insert(transaction);
        return buildResponse(transaction, balance.getAvailableAmount());
    }

    private void validateTransaction(Transaction transaction) {
        if (transaction.getAccountId() == null ) {
            LoggingUtility.LOGGER.error("Attempted to create transaction with null account ID.");
            throw new IllegalArgumentException("Account ID must not be null.");
        }
        if (transaction.getBalanceId() == null ) {
            LoggingUtility.LOGGER.error("Attempted to create transaction with null balance ID.");
            throw new IllegalArgumentException("Balance ID must not be null.");
        }
    }

    private void checkSufficientFunds(BigDecimal available, BigDecimal required) {
        if (available.compareTo(required) < 0) {
            throw new InsufficientFundsException("Insufficient funds for the transaction.");
        }
    }

    private Transaction createTransactionRecord(CreationTransactionDTO dto, Balance balance) {
        Transaction transaction = new Transaction();
        transaction.setAccountId(dto.getAccountId());
        transaction.setBalanceId(balance.getId());
        transaction.setAmount(dto.getAmount());
        transaction.setCurrency(dto.getCurrency());
        transaction.setDirection(dto.getDirection());
        transaction.setDescription(dto.getDescription());
        transaction.setDateCreated(OffsetDateTime.now());
        transaction.setLastUpdated(OffsetDateTime.now());
        return transaction;
    }

    @Transactional
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

    private ResponseTransactionDTO buildResponse(Transaction transaction, BigDecimal newBalance) {
        ResponseTransactionDTO response = new ResponseTransactionDTO();
        response.setTransactionId(transaction.getId());
        response.setAmount(transaction.getAmount());
        response.setCurrency(transaction.getCurrency());
        response.setDirection(transaction.getDirection());
        response.setDescription(transaction.getDescription());
        return response;
    }

    @Override
    public List<ResponseTransactionDTO> findAll(Long accountId, RowBounds rowBounds) {
        return List.of();
    }

    @Override
    public CreationTransactionDTO get(Long id) {
        return null;
    }

    @Override
    public ResponseTransactionDTO create(CreationTransactionDTO creationTransactionDTO) {
        return null;
    }
}
