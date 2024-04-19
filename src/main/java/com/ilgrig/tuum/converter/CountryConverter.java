package com.ilgrig.tuum.converter;

import com.ilgrig.tuum.domain.Country;
import com.ilgrig.tuum.model.CountryDTO;
import org.mapstruct.*;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CountryConverter {

    @Mapping(source = "code", target = "code")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "isoNumeric", target = "isoNumeric")
    CountryDTO toCountryDTO(Country country);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "code", target = "code")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "isoNumeric", target = "isoNumeric")
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "lastUpdated", ignore = true)
    Country fromCountryDTO(CountryDTO countryDTO);

    @AfterMapping
    default void ensureNonNullId(@MappingTarget Country country, CountryDTO countryDTO) {
        if (countryDTO.getId() != null) {
            country.setId(countryDTO.getId());
        }
    }
}