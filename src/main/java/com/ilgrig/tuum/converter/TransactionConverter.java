package com.ilgrig.tuum.converter;

import com.ilgrig.tuum.domain.Transaction;
import com.ilgrig.tuum.model.transaction.CreationTransactionDTO;
import com.ilgrig.tuum.model.transaction.ResponseTransactionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransactionConverter {

    TransactionConverter INSTANCE = Mappers.getMapper(TransactionConverter.class);

    @Mapping(target = "id", ignore = true) // Ignore ID during creation
    @Mapping(source = "accountId", target = "accountId")
    @Mapping(source = "currency", target = "currency")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "direction", target = "direction")
    @Mapping(source = "description", target = "description")
    @Mapping(target = "dateCreated", expression = "java(java.time.OffsetDateTime.now())")
    @Mapping(target = "lastUpdated", expression = "java(java.time.OffsetDateTime.now())")
    @Mapping(target = "balanceId", ignore = true) // Set in service
    Transaction toTransaction(CreationTransactionDTO dto);

    @Mapping(source = "accountId", target = "accountId")
    @Mapping(source = "id", target = "transactionId")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "currency", target = "currency")
    @Mapping(source = "direction", target = "direction")
    @Mapping(source = "description", target = "description")
    ResponseTransactionDTO toResponseDTO(Transaction transaction);
}
