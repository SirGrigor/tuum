package com.ilgrig.tuum.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Balance {

    private Long id;

    private BigDecimal availableAmount;

    private BigDecimal incomingTotal;

    private BigDecimal outgoingTotal;

    private Account account;

    private String currency;

    private Set<Transaction> transactions;

    private OffsetDateTime dateCreated;

    private OffsetDateTime lastUpdated;

}
