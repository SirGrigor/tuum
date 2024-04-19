package com.ilgrig.tuum.mapper;

import com.ilgrig.tuum.domain.Account;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AccountMapper {

    @Select("SELECT * FROM account")
    List<Account> findAll(RowBounds rowBounds);

    @Select("SELECT * FROM account WHERE id = #{id}")
    Optional<Account> findById(@Param("id") Long id);

    @Insert("INSERT INTO account (id, customer_id, country_id, date_created, last_updated)" +
            " VALUES (#{id}, #{customerId}, #{countryId}, #{dateCreated}, #{lastUpdated})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Account account);

    @Update("UPDATE account SET customer_id = #{customerId}, country_id = #{countryId} WHERE id = #{id}")
    void update(Account account);

}

