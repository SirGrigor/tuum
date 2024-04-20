package com.ilgrig.tuum.mapper;

import com.ilgrig.tuum.domain.Transaction;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TransactionMapper {
    @Select("SELECT * FROM transaction WHERE id = #{id}")
    Optional<Transaction> findById(@Param("id") Long id);

    @Select("SELECT * FROM transaction")
    List<Transaction> findAll(RowBounds rowBounds);

    @Insert("INSERT INTO transaction (amount, currency_id, account_id, direction, date_created, last_updated)" +
            " VALUES (#{amount}, #{currencyId}, #{accountId}, #{direction}, #{dateCreated}, #{lastUpdated})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Transaction transaction);

}
