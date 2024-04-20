package com.ilgrig.tuum.model.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class CreationTransactionDTO {

    @ValidAccount
    private Long accountId;

    @Schema(type = "string", example = "USD", allowableValues = {"SEK", "USD", "EUR", "GBP"})
    @Size(min = 3, max = 3)
    @NotNull
    private String currency;

    @PositiveAmount
    @Digits(integer = 18, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "92.08")
    private BigDecimal amount;

    @NotNull
    @Schema(type = "string", example = "OUT", allowableValues = {"IN", "OUT"})
    private TransactionDirection direction;

    @Size(max = 255)
    private String description;
}
