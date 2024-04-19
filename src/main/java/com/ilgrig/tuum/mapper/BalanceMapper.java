package com.ilgrig.tuum.mapper;

import com.ilgrig.tuum.domain.Balance;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Optional;


@Mapper
public interface BalanceMapper {

    @Select("SELECT * FROM balance WHERE id = #{id}")
    Optional<Balance> findById(@Param("id") Long id);

    @Select("SELECT * FROM balance")
    List<Balance> findAll(RowBounds rowBounds);

    @Select("SELECT * FROM balance WHERE code = #{code}")
    boolean existsByCurrencyCode(String code);

    @Insert("INSERT INTO balance (id, available_amount, account_id, currency_id, date_created, last_updated)" +
            " VALUES (#{id}, #{availableAmount}, #{accountId}, #{currencyId}, #{dateCreated}, #{lastUpdated})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Balance balance);

    @Update("UPDATE balance SET available_amount = #{availableAmount}, currency_id = #{currencyId}, account_id = #{accountId} WHERE id = #{id}")
    void update(Balance balance);

}
