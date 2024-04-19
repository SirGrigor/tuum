package com.ilgrig.tuum.mapper;

import com.ilgrig.tuum.domain.Currency;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CurrencyMapper {

    @Select("SELECT EXISTS(SELECT 1 FROM currency WHERE code = #{code})")
    boolean existsByCode(String code);

    @Select("SELECT EXISTS(SELECT 1 FROM currency WHERE name = #{name})")
    boolean existsByName(String name);

    @Select("SELECT EXISTS(SELECT 1 FROM currency WHERE symbol = #{symbol})")
    boolean existsBySymbol(String symbol);

    @Select("SELECT * FROM currency WHERE id = #{id}")
    Optional<Currency> findById(@Param("id") Long id);

    @Select("SELECT * FROM currency WHERE id = #{code}")
    Optional<Currency> findByCode(@Param("code") String code);

    @Select("SELECT * FROM currency")
    List<Currency> findAll(RowBounds rowBounds);

    @Insert("INSERT INTO currency (id, code, name, symbol, allowed, date_created, last_updated)" +
            " VALUES (#{id}, #{code}, #{name}, #{symbol}, #{allowed}, #{dateCreated}, #{lastUpdated})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Currency currency);

    @Update("UPDATE currency SET code = #{code}, name = #{name}, symbol = #{symbol}, allowed = #{allowed} WHERE id = #{id}")
    void update(Currency currency);

}
