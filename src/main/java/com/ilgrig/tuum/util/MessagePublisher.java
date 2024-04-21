package com.ilgrig.tuum.util;

import com.ilgrig.tuum.domain.Account;
import com.ilgrig.tuum.domain.Balance;
import com.ilgrig.tuum.domain.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MessagePublisher {

    private final RabbitTemplate rabbitTemplate;
    private static final String QUEUE_NAME = "transactionQueue";

    private static final String ACCOUNT_ID = "accountId";
    private static final String EVENT_TYPE = "eventType";
    private static final String CURRENCY = "currency";
    private static final String AMOUNT = "amount";
    private static final String DIRECTION = "direction";
    private static final String BALANCE_ID = "balanceId";
    private static final String AVAILABLE_AMOUNT = "availableAmount";
    private static final String NEW_BALANCE = "newBalance";
    private static final String LAST_UPDATED = "lastUpdated";

    private static final String ACCOUNT_CREATED = "Account Created";
    private static final String BALANCE_CREATED = "Balance Created";
    private static final String TRANSACTION_CREATED = "Transaction Created";
    private static final String BALANCE_UPDATED = "Balance Updated";

    public void publishAccountCreated(Account account, List<String> currencies) {
        Map<String, Object> messageDetails = new HashMap<>();
        messageDetails.put(ACCOUNT_ID, account.getId());
        messageDetails.put("currencies", currencies);
        messageDetails.put(EVENT_TYPE, ACCOUNT_CREATED);
        rabbitTemplate.convertAndSend(QUEUE_NAME, messageDetails);
    }

    public void publishBalanceCreated(Balance balance) {
        Map<String, Object> messageDetails = new HashMap<>();
        messageDetails.put(ACCOUNT_ID, balance.getAccount().getId());
        messageDetails.put(BALANCE_ID, balance.getId());
        messageDetails.put(CURRENCY, balance.getCurrency());
        messageDetails.put(AVAILABLE_AMOUNT, balance.getAvailableAmount());
        messageDetails.put(EVENT_TYPE, BALANCE_CREATED);
        rabbitTemplate.convertAndSend(QUEUE_NAME, messageDetails);
    }

    public void publishTransactionCreated(Transaction transaction) {
        Map<String, Object> message = new HashMap<>();
        message.put(ACCOUNT_ID, transaction.getAccountId());
        message.put("transactionId", transaction.getId());
        message.put(AMOUNT, transaction.getAmount());
        message.put(CURRENCY, transaction.getCurrency());
        message.put(DIRECTION, transaction.getDirection().toString());
        message.put("balanceAfter", transaction.getBalanceAfterTransaction());
        message.put(EVENT_TYPE, TRANSACTION_CREATED);
        rabbitTemplate.convertAndSend(QUEUE_NAME, message);
    }

    public void publishBalanceUpdated(Balance balance) {
        Map<String, Object> message = new HashMap<>();
        message.put(BALANCE_ID, balance.getId());
        message.put(NEW_BALANCE, balance.getAvailableAmount());
        message.put(LAST_UPDATED, balance.getLastUpdated());
        message.put(CURRENCY, balance.getCurrency());
        message.put(EVENT_TYPE, BALANCE_UPDATED);
        rabbitTemplate.convertAndSend(QUEUE_NAME, message);
    }
}
