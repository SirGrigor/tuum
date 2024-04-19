package com.ilgrig.tuum.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Country {

    private Long id;

    private String code;

    private String name;

    private String isoNumeric;

    private Account account;

    private OffsetDateTime dateCreated;

    private OffsetDateTime lastUpdated;

}
