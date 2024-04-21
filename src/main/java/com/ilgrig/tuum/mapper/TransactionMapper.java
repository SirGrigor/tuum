package com.ilgrig.tuum.mapper;

import com.ilgrig.tuum.domain.Transaction;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface TransactionMapper {
    @Insert("INSERT INTO transaction (amount, balance_after_transaction, currency, direction, description, account_id, balance_id) " +
            "VALUES (#{amount},  #{balanceAfterTransaction}, #{currency}, #{direction}, #{description}, #{accountId}, #{balanceId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Transaction transaction);

    @Select("SELECT id, amount, balance_after_transaction, currency, direction, description, account_id, balance_id, date_created, last_updated FROM transaction WHERE account_id = #{accountId}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "amount", column = "amount"),
            @Result(property = "balanceAfterTransaction", column = "balance_after_transaction"),
            @Result(property = "currency", column = "currency"),
            @Result(property = "direction", column = "direction"),
            @Result(property = "description", column = "description"),
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "balanceId", column = "balance_id"),
    })
    List<Transaction> findAllByAccountId(@Param("accountId") Long accountId, RowBounds rowBounds);
}
