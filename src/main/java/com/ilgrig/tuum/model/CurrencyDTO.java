package com.ilgrig.tuum.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class CurrencyDTO {

    private Long id;

    @Size(max = 10)
    private String code;

    @Size(max = 255)
    private String name;

    @Size(max = 10)
    private String symbol;

    @NotNull
    @JsonProperty("allowed")
    private Boolean allowed;

}
