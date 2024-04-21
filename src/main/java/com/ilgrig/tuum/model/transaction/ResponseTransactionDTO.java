package com.ilgrig.tuum.model.transaction;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ResponseTransactionDTO {

    private Long accountId;
    private Long transactionId;
    private BigDecimal amount;
    private BigDecimal balanceAfterTransaction;
    private String currency;
    private TransactionDirection direction;
    private String description;

}
