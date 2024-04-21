package com.ilgrig.tuum.domain;

import com.ilgrig.tuum.model.transaction.TransactionDirection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    private Long id;

    private BigDecimal amount;

    private BigDecimal balanceAfterTransaction;

    private TransactionDirection direction;

    private Long accountId;

    private Long balanceId;

    private String description;

    private String currency;

    private OffsetDateTime dateCreated;

    private OffsetDateTime lastUpdated;

}
