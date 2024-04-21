package com.ilgrig.tuum.util;

import com.ilgrig.tuum.domain.Account;
import com.ilgrig.tuum.domain.Balance;
import com.ilgrig.tuum.domain.Transaction;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MessagePublisher {

    private final RabbitTemplate rabbitTemplate;
    private static final String QUEUE_NAME = "transactionQueue";

    @Autowired
    public MessagePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishAccountCreated(Account account, List<String> currencies) {
        Map<String, Object> messageDetails = new HashMap<>();
        messageDetails.put("accountId", account.getId());
        messageDetails.put("currencies", currencies);
        messageDetails.put("eventType", "Account Created");
        rabbitTemplate.convertAndSend(QUEUE_NAME, messageDetails);
    }

    public void publishBalanceCreated(Balance balance) {
        Map<String, Object> messageDetails = new HashMap<>();
        messageDetails.put("accountId", balance.getAccount().getId());
        messageDetails.put("balanceId", balance.getId());
        messageDetails.put("currency", balance.getCurrency());
        messageDetails.put("availableAmount", balance.getAvailableAmount());
        messageDetails.put("eventType", "Balance Created");
        rabbitTemplate.convertAndSend(QUEUE_NAME, messageDetails);
    }

    public void publishTransactionCreated(Transaction transaction) {
        Map<String, Object> message = new HashMap<>();
        message.put("accountId", transaction.getAccountId());
        message.put("transactionId", transaction.getId());
        message.put("amount", transaction.getAmount());
        message.put("currency", transaction.getCurrency());
        message.put("direction", transaction.getDirection().toString());
        message.put("balanceAfter", transaction.getBalanceAfterTransaction());
        message.put("eventType", "Transaction Created");
        rabbitTemplate.convertAndSend(QUEUE_NAME, message);
    }

    public void publishBalanceUpdated(Balance balance) {
        Map<String, Object> message = new HashMap<>();
        message.put("balanceId", balance.getId());
        message.put("newBalance", balance.getAvailableAmount());
        message.put("lastUpdated", balance.getLastUpdated());
        message.put("currency", balance.getCurrency());
        message.put("eventType", "Balance Updated");
        rabbitTemplate.convertAndSend(QUEUE_NAME, message);
    }
}
