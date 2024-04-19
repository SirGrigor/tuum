package com.ilgrig.tuum.converter;

import com.ilgrig.tuum.domain.Balance;
import com.ilgrig.tuum.model.balance.CreationBalanceDTO;
import com.ilgrig.tuum.model.balance.ResponseBalanceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BalanceConverter {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "availableAmount", target = "amount")
    @Mapping(source = "currency.code", target = "currency")
    ResponseBalanceDTO balanceToResponseBalanceDTO(Balance balance);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "availableAmount", target = "availableAmount")
    @Mapping(source = "account", target = "account.id")
    @Mapping(source = "currency", target = "currency.id")
    Balance creationBalanceDTOToBalance(CreationBalanceDTO creationBalanceDTO);

}

