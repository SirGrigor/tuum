package com.ilgrig.tuum.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    private Long id;

    private Long customerId;

    private String country;

    private Set<Balance> balances;

    private Set<Transaction> transactions;

    private OffsetDateTime dateCreated;

    private OffsetDateTime lastUpdated;

}
