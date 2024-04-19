package com.ilgrig.tuum.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Balance {

    private Long id;

    private BigDecimal availableAmount;

    private Account account;

    private Currency currency;

    private OffsetDateTime dateCreated;

    private OffsetDateTime lastUpdated;

}
