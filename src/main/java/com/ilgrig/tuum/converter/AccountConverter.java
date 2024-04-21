package com.ilgrig.tuum.converter;

import com.ilgrig.tuum.domain.Account;
import com.ilgrig.tuum.domain.Balance;
import com.ilgrig.tuum.model.account.CreationAccountDTO;
import com.ilgrig.tuum.model.account.ResponseAccountDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface AccountConverter {

    AccountConverter INSTANCE = Mappers.getMapper(AccountConverter.class);
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "customerId", target = "customerId")
    @Mapping(source = "country", target = "country")
    @Mapping(target = "balances", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "dateCreated", expression = "java(java.time.OffsetDateTime.now())")
    @Mapping(target = "lastUpdated", expression = "java(java.time.OffsetDateTime.now())")
    Account creationAccountDTOToAccount(CreationAccountDTO creationAccountDTO);

    @Mapping(source = "id", target = "accountId")
    @Mapping(source = "customerId", target = "customerId")
    @Mapping(source = "balances", target = "balances")
    ResponseAccountDTO accountToResponseAccountDTO(Account account);

    @AfterMapping
    default void fillBalances(@MappingTarget Account account, CreationAccountDTO creationAccountDTO) {
        Set<Balance> balances = creationAccountDTO.getCurrencies().stream()
                .map(currencyCode -> {
                    Balance balance = new Balance();
                    balance.setCurrency(currencyCode);
                    balance.setAvailableAmount(BigDecimal.ZERO);
                    balance.setDateCreated(OffsetDateTime.now());
                    balance.setLastUpdated(OffsetDateTime.now());
                    return balance;
                }).collect(Collectors.toSet());
        account.setBalances(balances);
    }
}

