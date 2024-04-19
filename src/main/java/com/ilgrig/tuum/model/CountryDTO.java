package com.ilgrig.tuum.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class CountryDTO {

    private Long id;

    @Size(max = 3)
    private String code;

    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String isoNumeric;

}
