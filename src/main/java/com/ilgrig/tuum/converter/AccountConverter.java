package com.ilgrig.tuum.converter;

import com.ilgrig.tuum.domain.Account;
import com.ilgrig.tuum.domain.Balance;
import com.ilgrig.tuum.domain.Country;
import com.ilgrig.tuum.domain.Currency;
import com.ilgrig.tuum.mapper.CountryMapper;
import com.ilgrig.tuum.mapper.CurrencyMapper;
import com.ilgrig.tuum.model.account.CreationAccountDTO;
import com.ilgrig.tuum.util.NotFoundException;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {BalanceConverter.class, CountryMapper.class, CurrencyMapper.class})
public interface AccountConverter {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "customerId", target = "customerId")
    @Mapping(target = "country", source = "country", qualifiedByName = "countryCodeToCountry")
    @Mapping(target = "balances", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "lastUpdated", ignore = true)
    Account creationAccountDTOToAccount(CreationAccountDTO creationAccountDTO);

    @AfterMapping
    default void enhanceAccountWithComplexObjects(@MappingTarget Account account, CreationAccountDTO creationAccountDTO,
                                                  @Context CountryMapper countryMapper, @Context CurrencyMapper currencyMapper) {
        if (creationAccountDTO.getCountry() != null) {
            Country country = countryMapper.findByCode(creationAccountDTO.getCountry())
                    .orElseThrow(() -> new NotFoundException("Country with code " + creationAccountDTO.getCountry() + " not found"));
            account.setCountry(country);
        }

        if (creationAccountDTO.getCurrencies() != null) {
            Set<Balance> balances = creationAccountDTO.getCurrencies().stream()
                    .map(currencyCode -> {
                        Currency currency = currencyMapper.findByCode(currencyCode)
                                .orElseThrow(() -> new NotFoundException("Currency with code " + currencyCode + " not found"));
                        Balance balance = new Balance();
                        balance.setCurrency(currency);
                        balance.setAvailableAmount(BigDecimal.valueOf(10.35));  // Example value
                        balance.setDateCreated(OffsetDateTime.now());
                        balance.setLastUpdated(OffsetDateTime.now());
                        return balance;
                    })
                    .collect(Collectors.toSet());
            account.setBalances(balances);
        }
    }
}
