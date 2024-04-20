package com.ilgrig.tuum.mapper;

import com.ilgrig.tuum.domain.Balance;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Mapper
public interface BalanceMapper {

    @Select("SELECT * FROM balance WHERE id = #{id}")
    Optional<Balance> findById(@Param("id") Long id);

    @Select("SELECT * FROM balance")
    List<Balance> findAll();

    @Insert("INSERT INTO balance (available_amount, currency, account_id)" +
            " VALUES (#{availableAmount}, #{currency}, #{account.id})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Balance balance);

    @Update("UPDATE balance SET available_amount = #{availableAmount} WHERE id = #{id}")
    void update(Balance balance);

    @Select("SELECT * FROM balance WHERE currency = #{currency}")
    Balance findByCurrency(Balance balance);

}
