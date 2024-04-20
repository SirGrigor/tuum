package com.ilgrig.tuum.model.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class CreationAccountDTO {

    @NotNull
    private Long customerId;

    @NotNull
    @Schema(description = "Country code", example = "SE")
    @ValidCountry
    private String country;

    @NotNull
    @Schema(description = "List of currency codes", example = "[\"SEK\", \"USD\", \"EUR\", \"GBP\"]")
    @ValidCurrency
    private List<String> currencies;

}
