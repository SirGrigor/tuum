package com.ilgrig.tuum.model.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class CreationTransactionDTO {
    @NotNull
    @Schema(type = "Long", example = "1", description = "Account ID")
    private Long accountId;

    @PositiveAmount
    @Digits(integer = 18, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "92.08")
    @NotNull
    private BigDecimal amount;

    @Schema(type = "string", example = "USD", allowableValues = {"SEK", "USD", "EUR", "GBP"})
    @NotNull
    @ValidTransactionCurrency
    private String currency;

    @NotNull
    @Schema(type = "string", example = "OUT", allowableValues = {"IN", "OUT"})
    private TransactionDirection direction;

    @Size(max = 255)
    @Schema(type = "string", example = "Payment for the services")
    @NotNull
    @NotEmpty
    private String description;
}
