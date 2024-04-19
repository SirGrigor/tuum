package com.ilgrig.tuum.converter;

import com.ilgrig.tuum.domain.Currency;
import com.ilgrig.tuum.model.CurrencyDTO;
import org.mapstruct.*;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CurrencyConverter {

    Currency findByCode(String code);

    @Mapping(source = "code", target = "code")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "symbol", target = "symbol")
    CurrencyDTO toCurrencyDTO(Currency currency);
}