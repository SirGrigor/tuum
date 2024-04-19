package com.ilgrig.tuum.mapper;

import com.ilgrig.tuum.domain.Country;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CountryMapper {

    @Select("SELECT EXISTS(SELECT 1 FROM country WHERE code = #{cóode})")
    boolean existsByCode(String code);

    @Select("SELECT EXISTS(SELECT 1 FROM country WHERE iso_numeric = #{isoNumeric})")
    boolean existsByIsoNumeric(String isoNumeric);

    @Select("SELECT * FROM country WHERE id = #{id}")
    Optional<Country> findById(Long id);

    @Select("SELECT * FROM country WHERE code = #{code}")
    Optional<Country> findByCode(String code);

    @Select("SELECT * FROM country")
    List<Country> findAll();

    @Update("UPDATE country SET code = #{code}, name = #{name}, iso_numeric = #{isoNumeric} WHERE id = #{id}")
    void update(Country country);
}