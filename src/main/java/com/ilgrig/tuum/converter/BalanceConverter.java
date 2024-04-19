package com.ilgrig.tuum.converter;

import com.ilgrig.tuum.domain.Account;
import com.ilgrig.tuum.domain.Balance;
import com.ilgrig.tuum.mapper.AccountMapper;
import com.ilgrig.tuum.mapper.CurrencyMapper;
import com.ilgrig.tuum.model.BalanceDTO;
import com.ilgrig.tuum.util.NotFoundException;
import com.ilgrig.tuum.domain.Currency;
import org.mapstruct.*;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BalanceConverter {

    @Mapping(source = "account.id", target = "account")
    @Mapping(source = "currency.id", target = "currency")
    BalanceDTO toBalanceDTO(Balance balance);

    @Mapping(target = "account", ignore = true)
    @Mapping(target = "currency", ignore = true)
    @Mapping(target = "availableAmount", source = "availableAmount")
    Balance fromBalanceDTO(BalanceDTO balanceDTO);

    @AfterMapping
    default void fromBalanceDTOAfterMapping(@MappingTarget Balance balance, BalanceDTO balanceDTO,
                                            @Context AccountMapper accountMapper,
                                            @Context CurrencyMapper currencyMapper) {
        if (balanceDTO.getAccount() != null) {
            Account account = accountMapper.findById(balanceDTO.getAccount())
                    .orElseThrow(() -> new NotFoundException("Account not found"));
            balance.setAccount(account);
        }
        if (balanceDTO.getCurrency() != null) {
            Currency currency = currencyMapper.findById(balanceDTO.getCurrency())
                    .orElseThrow(() -> new NotFoundException("Currency not found"));
            balance.setCurrency(currency);
        }
    }
}

