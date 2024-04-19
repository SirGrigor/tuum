package com.ilgrig.tuum.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class BalanceDTO {

    private Long id;

    @Digits(integer = 18, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "95.08")
    private BigDecimal availableAmount;

    private Long account;

    private Long currency;

}
