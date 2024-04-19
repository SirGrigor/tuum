package com.ilgrig.tuum.converter;

import com.ilgrig.tuum.domain.Account;
import com.ilgrig.tuum.domain.Country;
import com.ilgrig.tuum.mapper.CountryMapper;
import com.ilgrig.tuum.model.AccountDTO;
import com.ilgrig.tuum.util.NotFoundException;
import org.mapstruct.*;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountConverter {

    @Mapping(source = "country.id", target = "country")
    AccountDTO toAccountDTO(Account account);

    @Mapping(target = "country", ignore = true)
    Account fromAccountDTO(AccountDTO accountDTO);

    @AfterMapping
    default void fromAccountDTOAfterMapping(@MappingTarget Account account, AccountDTO accountDTO,
                                            @Context CountryMapper countryMapper) {
        if (accountDTO.getCountry() != null) {
            Country country = countryMapper.findById(accountDTO.getCountry())
                    .orElseThrow(() -> new NotFoundException("Country not found"));
            account.setCountry(country);
        }
    }
}
