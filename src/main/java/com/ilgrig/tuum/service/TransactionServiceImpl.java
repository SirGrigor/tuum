package com.ilgrig.tuum.service;

import com.ilgrig.tuum.converter.TransactionConverter;
import com.ilgrig.tuum.domain.Balance;
import com.ilgrig.tuum.domain.Transaction;
import com.ilgrig.tuum.mapper.BalanceMapper;
import com.ilgrig.tuum.mapper.TransactionMapper;
import com.ilgrig.tuum.model.transaction.CreationTransactionDTO;
import com.ilgrig.tuum.model.transaction.ResponseTransactionDTO;
import com.ilgrig.tuum.model.transaction.TransactionDirection;
import com.ilgrig.tuum.util.BalanceNotFoundException;
import com.ilgrig.tuum.util.InsufficientFundsException;
import com.ilgrig.tuum.util.MessagePublisher;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);
    private final TransactionMapper transactionMapper;
    private final BalanceMapper balanceMapper;
    private final TransactionConverter transactionConverter;
    private final MessagePublisher messagePublisher;
    private final AccountService accountService;

    @Transactional
    @Override
    public ResponseTransactionDTO createTransaction(CreationTransactionDTO dto) {
        log.debug("Creating transaction for account ID {} and amount {}", dto.getAccountId(), dto.getAmount());

        accountService.validateAccountExistence(dto.getAccountId());

        Balance balance = findBalanceByAccountIdAndCurrency(dto.getAccountId(), dto.getCurrency());
        validateFundsForTransaction(dto, balance);

        Transaction transaction = transactionConverter.toTransaction(dto);
        transaction.setBalanceId(balance.getId());
        transaction.setBalanceAfterTransaction(updateBalance(balance, dto.getAmount(), dto.getDirection()));
        transactionMapper.insert(transaction);
        messagePublisher.publishTransactionCreated(transaction);

        log.debug("Transaction created successfully for account ID {}", dto.getAccountId());
        return transactionConverter.toResponseDTO(transaction);
    }

    private void validateFundsForTransaction(CreationTransactionDTO dto, Balance balance) {
        if (dto.getDirection() == TransactionDirection.OUT && balance.getAvailableAmount().compareTo(dto.getAmount()) < 0) {
            log.error("Insufficient funds for transaction: Account ID {}, Requested {}, Available {}",
                    dto.getAccountId(), dto.getAmount(), balance.getAvailableAmount());
            throw new InsufficientFundsException("Insufficient funds for the transaction.");
        }
    }

    private Balance findBalanceByAccountIdAndCurrency(Long accountId, String currency) {
        return balanceMapper.findBalanceByAccountIdAndCurrency(accountId, currency)
                .orElseThrow(() -> new BalanceNotFoundException("Balance not found for account ID " + accountId + " and currency " + currency));
    }

    public BigDecimal updateBalance(Balance balance, BigDecimal amount, TransactionDirection direction) {
        BigDecimal newAmount = direction == TransactionDirection.IN ?
                balance.getAvailableAmount().add(amount) :
                balance.getAvailableAmount().subtract(amount);
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
                .collect(Collectors.toList());
    }
}
