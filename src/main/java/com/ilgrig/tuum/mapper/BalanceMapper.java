package com.ilgrig.tuum.mapper;

import com.ilgrig.tuum.domain.Balance;
import org.apache.ibatis.annotations.*;

import java.util.Set;


@Mapper
public interface BalanceMapper {

    @Insert("INSERT INTO balance (available_amount, currency, account_id)" +
            " VALUES (#{availableAmount}, #{currency}, #{account.id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Balance balance);

    @Select("SELECT id, available_amount, currency, date_created, last_updated " +
            "FROM balance WHERE account_id = #{accountId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "availableAmount", column = "available_amount"),
            @Result(property = "currency", column = "currency"),
            @Result(property = "dateCreated", column = "date_created"),
            @Result(property = "lastUpdated", column = "last_updated")
    })
    Set<Balance> findBalancesByAccountId(@Param("accountId") Long accountId);

    @Select("SELECT id, available_amount, currency " +
            "FROM balance WHERE account_id = #{accountId} AND currency = #{currency}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "availableAmount", column = "available_amount"),
            @Result(property = "currency", column = "currency"),
    })
    Balance findBalanceByAccountIdAndCurrency(@Param("accountId") Long accountId, @Param("currency") String currency);

    @Update("UPDATE balance SET available_amount = #{availableAmount}, last_updated = #{lastUpdated} " +
            "WHERE id = #{id}")
    void update(Balance balance);

}
