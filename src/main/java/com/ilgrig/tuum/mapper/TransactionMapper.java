package com.ilgrig.tuum.mapper;

import com.ilgrig.tuum.domain.Transaction;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TransactionMapper {
    @Insert("INSERT INTO transaction (amount, currency, direction, description, account_id, balance_id) " +
            "VALUES (#{amount}, #{currency}, #{direction}, #{description}, #{accountId}, #{balanceId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Transaction transaction);

    @Select("SELECT * FROM transaction WHERE account_id = #{accountId}")
    List<Transaction> findAllByAccountId(Long accountId);
}
