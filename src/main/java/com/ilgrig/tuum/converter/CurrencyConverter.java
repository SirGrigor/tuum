package com.ilgrig.tuum.converter;

import com.ilgrig.tuum.domain.Currency;
import com.ilgrig.tuum.model.CurrencyDTO;
import org.mapstruct.*;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CurrencyConverter {

    @Mapping(source = "code", target = "code")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "symbol", target = "symbol")
    @Mapping(source = "allowed", target = "allowed")
    CurrencyDTO toCurrencyDTO(Currency currency);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "code", target = "code")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "symbol", target = "symbol")
    @Mapping(source = "allowed", target = "allowed")
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "transaction", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "lastUpdated", ignore = true)
    Currency fromCurrencyDTO(CurrencyDTO currencyDTO);

    @AfterMapping
    default void ensureNonNullFields(@MappingTarget Currency currency, CurrencyDTO currencyDTO) {
        if (currencyDTO.getId() != null) {
            currency.setId(currencyDTO.getId());
        }
    }
}