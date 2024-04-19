package com.ilgrig.tuum.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency {

    private Long id;

    private String code;

    private String name;

    private String symbol;

    private Boolean allowed;

    private Balance balance;

    private Transaction transaction;

    private OffsetDateTime dateCreated;

    private OffsetDateTime lastUpdated;

}
