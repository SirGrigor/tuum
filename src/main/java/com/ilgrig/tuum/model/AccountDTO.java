package com.ilgrig.tuum.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AccountDTO {

    private Long id;

    @NotNull
    private Long customerId;

    @NotNull
    private Long country;

}
