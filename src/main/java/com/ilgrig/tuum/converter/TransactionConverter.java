package com.ilgrig.tuum.converter;

import com.ilgrig.tuum.domain.Account;
import com.ilgrig.tuum.domain.Currency;
import com.ilgrig.tuum.domain.Transaction;
import com.ilgrig.tuum.mapper.AccountMapper;
import com.ilgrig.tuum.mapper.CurrencyMapper;
import com.ilgrig.tuum.model.transaction.CreationTransactionDTO;
import com.ilgrig.tuum.util.NotFoundException;
import org.mapstruct.*;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransactionConverter {

    @Mapping(source = "account.id", target = "account")
    @Mapping(source = "currency.id", target = "currency")
    CreationTransactionDTO toTransactionDTO(Transaction transaction);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "currency", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "lastUpdated", ignore = true)
    Transaction fromTransactionDTO(CreationTransactionDTO creationTransactionDTO);

    @AfterMapping
    default void fromTransactionDTOAfterMapping(CreationTransactionDTO creationTransactionDTO,
                                                @MappingTarget Transaction transaction,
                                                @Context AccountMapper accountMapper,
                                                @Context CurrencyMapper currencyMapper) {
        if (creationTransactionDTO.getAccount() != null) {
            Account account = accountMapper.findById(creationTransactionDTO.getAccount())
                    .orElseThrow(() -> new NotFoundException("Account not found"));
            transaction.setAccount(account);
        }
        if (creationTransactionDTO.getCurrency() != null) {
            Currency currency = currencyMapper.findById(creationTransactionDTO.getCurrency())
                    .orElseThrow(() -> new NotFoundException("Currency not found"));
            transaction.setCurrency(currency);
        }
    }
}
