package com.ilgrig.tuum.converter;

import com.ilgrig.tuum.domain.Balance;
import com.ilgrig.tuum.model.balance.CreationBalanceDTO;
import com.ilgrig.tuum.model.balance.ResponseBalanceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BalanceConverter {

    BalanceConverter INSTANCE = Mappers.getMapper(BalanceConverter.class);


    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "availableAmount", target = "availableAmount"),
            @Mapping(source = "currency", target = "currency")
    })
    ResponseBalanceDTO toResponseBalanceDTO(Balance balance);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "availableAmount", target = "availableAmount"),
            @Mapping(source = "currency", target = "currency"),
            @Mapping(target = "account", ignore = true),
            @Mapping(target = "incomingTotal", ignore = true),
            @Mapping(target = "outgoingTotal", ignore = true),
            @Mapping(target = "transactions", ignore = true),
            @Mapping(target = "dateCreated", ignore = true),
            @Mapping(target = "lastUpdated", ignore = true)
    })
    Balance toBalance(CreationBalanceDTO creationBalanceDTO);

}

