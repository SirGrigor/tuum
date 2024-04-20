package com.ilgrig.tuum.mapper;

import com.ilgrig.tuum.domain.Account;
import com.ilgrig.tuum.domain.Balance;
import org.apache.ibatis.annotations.*;

import java.util.Optional;
import java.util.Set;

@Mapper
public interface AccountMapper {

    @Select("SELECT * FROM account WHERE id = #{id}")
    Optional<Account> findById(@Param("id") Long id);

    @Insert("INSERT INTO account (customer_id, country, date_created, last_updated)" +
            " VALUES (#{customerId}, #{country}, #{dateCreated}, #{lastUpdated})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Account account);

    @Update("UPDATE account SET customer_id = #{customerId}, country = #{country}, last_updated = #{lastUpdated} WHERE id = #{id}")
    void update(Account account);

    @Select("SELECT EXISTS(SELECT 1 FROM account WHERE customer_id = #{customerId})")
    boolean existsByCustomerId(Long customerId);

    @Select("SELECT id AS account_id, customer_id, country, date_created, last_updated " +
            "FROM account WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "account_id"),
            @Result(property = "customerId", column = "customer_id"),
            @Result(property = "country", column = "country"),
            @Result(property = "dateCreated", column = "date_created"),
            @Result(property = "lastUpdated", column = "last_updated")
    })
    Optional<Account> findAccountById(@Param("id") Long id);

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
}

