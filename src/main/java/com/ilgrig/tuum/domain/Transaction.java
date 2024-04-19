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

    private TransactionDirection direction;

    private String description;

    private Account account;

    private Currency currency;

    private OffsetDateTime dateCreated;

    private OffsetDateTime lastUpdated;

}
