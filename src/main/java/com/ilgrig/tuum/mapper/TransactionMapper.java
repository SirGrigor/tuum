package com.ilgrig.tuum.mapper;

import com.ilgrig.tuum.domain.Transaction;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TransactionMapper {
    String SELECT_FROM_TRANSACTION_WHERE_ID = "SELECT * FROM transaction WHERE id = #{id}";

    @Select(SELECT_FROM_TRANSACTION_WHERE_ID)
    Optional<Transaction> findById(@Param("id") Long id);

    @Select("SELECT * FROM transaction WHERE account_id = #{id}")
    List<Transaction> findAllById(@Param("id") Long id, RowBounds rowBounds);

    @Select("SELECT * FROM transaction")
    List<Transaction> findAll(RowBounds rowBounds);

    @Insert("INSERT INTO transaction (id, amount, currency_id, account_id, direction, date_created, last_updated)" +
            " VALUES (#{id}, #{amount}, #{currencyId}, #{accountId}, #{direction}, #{dateCreated}, #{lastUpdated})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Transaction transaction);

    @Update("UPDATE transaction SET amount = #{amount}, currency_id = #{currencyId}, account_id = #{accountId} WHERE id = #{id}")
    void update(Transaction transaction);

}
