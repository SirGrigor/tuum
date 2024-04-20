package com.ilgrig.tuum.mapper;

import com.ilgrig.tuum.domain.Account;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

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

    @Select("SELECT * FROM account WHERE customer_id = #{customerId}")
    Optional<Account> findByCustomerId(@Param("customerId") Long customerId);

}

