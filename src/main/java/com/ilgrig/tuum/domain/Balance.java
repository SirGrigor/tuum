package com.ilgrig.tuum.domain;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Balance {

    private Long id;

    private BigDecimal availableAmount;

    private Account account;

    private String currency;

    private Set<Transaction> transactions;

    private OffsetDateTime dateCreated;

    private OffsetDateTime lastUpdated;

}
