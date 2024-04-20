package com.ilgrig.tuum.model.balance;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ResponseBalanceDTO {

    @NotNull
    Long id;

    @NotNull
    @Digits(integer = 18, fraction = 2)
    @Schema(description = "Available balance", example = "99.85")
    BigDecimal availableAmount;

    @NotNull
    @Schema(description = "Currency code", example = "SEK")
    String currency;

}
