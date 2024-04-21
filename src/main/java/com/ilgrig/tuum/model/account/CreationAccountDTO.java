package com.ilgrig.tuum.model.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class CreationAccountDTO {

    @NotNull
    @Schema(type = "Long", example = "1", description = "Customer ID")
    private Long customerId;

    @NotNull
    @Schema(description = "Country code", example = "SE")
    @ValidCountry
    private String country;

    @NotNull
    @Schema(description = "List of currency codes", example = "[\"SEK\", \"USD\", \"EUR\", \"GBP\"]", allowableValues = {"SEK", "USD", "EUR", "GBP"})
    @ValidCurrency
    @Size(min = 1, max = 4)
    private List<String> currencies;

}
